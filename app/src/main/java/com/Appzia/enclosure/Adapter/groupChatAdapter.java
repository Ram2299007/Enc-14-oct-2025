package com.Appzia.enclosure.Adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.linkPreviewModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.Screens.multiple_show_image_screen;
import com.Appzia.enclosure.Utils.MultipleImageDialogHelper;
import com.Appzia.enclosure.Utils.BlurImageOptimizer;
import com.Appzia.enclosure.models.members;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.show_document_screen;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Screens.show_video_playerScreen;
import com.Appzia.enclosure.Utils.BlurHelper;
import com.Appzia.enclosure.Utils.Bottomshit.MusicPlayerBottomSheet;
import com.Appzia.enclosure.Utils.Bottomshit.MusicPlayerBottomSheetGroup;
import com.Appzia.enclosure.Utils.BroadcastReiciver.AudioPlaybackService;
import com.Appzia.enclosure.Utils.BroadcastReiciver.AudioPlaybackServiceGroup;
import com.Appzia.enclosure.Utils.Constant;
import com.Appzia.enclosure.Utils.MediaPlayerManager;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.Webservice;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
// import com.squareup.picasso.Picasso; // No longer needed - using Constant methods
// import com.squareup.picasso.Target; // No longer needed - using Constant methods

import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;

public class groupChatAdapter extends RecyclerView.Adapter {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(groupChatAdapter.class);
    Context mContext;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    public static int checkBarIsActive = 0;
    RequestOptions requestOptions = new RequestOptions();
    ColorStateList tintList;
    String themColor;
    private static boolean loadHighQuality = true;
    public static long downloadId;
    ArrayList<group_messageModel> groupMessageList = new ArrayList<>();
    private boolean isLastItemVisible;
    AppCompatActivity activity;
    FirebaseDatabase database;
    String grpIdKey, name, caption;
    int temppostion = 0;
    int temptotalduration = 0;
    CardView valuable;
    private WeakReference<RecyclerView> recyclerViewReference;
    private MediaPlayerCallback mediaPlayerCallback;
    // pass activity object here


    public void setHighQualityLoading(boolean highQuality) {
        if (loadHighQuality != highQuality) {
            loadHighQuality = highQuality;
            Log.d("ChatAdapter", "High quality loading set to: " + highQuality);

            RecyclerView recyclerView = recyclerViewReference.get();
            if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
                if (animator instanceof DefaultItemAnimator) {
                    DefaultItemAnimator defaultAnimator = (DefaultItemAnimator) animator;
                    defaultAnimator.setSupportsChangeAnimations(false);
                    defaultAnimator.setChangeDuration(0);
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int first = layoutManager.findFirstVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();
                // Only notify if there are visible items
                if (first <= last && first != RecyclerView.NO_POSITION) {
                    notifyItemRangeChanged(first, last - first + 1);
                    Log.d("ChatAdapter", "Notified item range changed: " + first + " to " + last);
                }
            }
        }
    }

    public groupChatAdapter(Context mContext, ArrayList<group_messageModel> groupMessageList, AppCompatActivity activity, CardView valuable, RecyclerView messageRecView, String grpIdKey, String name, String caption) {
        this.mContext = mContext;
        this.groupMessageList = groupMessageList;
        this.activity = activity;
        this.valuable = valuable;
        this.isLastItemVisible = false;
        this.recyclerViewReference = new WeakReference<>(messageRecView);
        this.grpIdKey = grpIdKey;
        this.name = name;
        this.caption = caption;
    }

    public groupChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void removeItem(int adapterPosition) {

        try {
            groupMessageList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        } catch (Exception e) {

        }

        if (groupMessageList.size() == 0) {

            if (valuable.getVisibility() == View.GONE) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(2000);
                valuable.startAnimation(fadeIn);
                valuable.setVisibility(View.VISIBLE);
            }

        } else {
            if (valuable.getVisibility() == View.VISIBLE) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(2000);
                valuable.startAnimation(fadeOut);
                valuable.setVisibility(View.GONE);
            }
        }


    }


    public void setLastItemVisible(boolean isVisible) {
        this.isLastItemVisible = isVisible;

        try {
            notifyItemChanged(groupMessageList.size() - 1);
        } catch (Exception ignored) {
            Toast.makeText(mContext, "notifyItemChanged" + ignored.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Only refresh the last item
    }

    public void enableStableIds() {
        try {
            setHasStableIds(true);
        } catch (Exception ignored) {
        }
    }

    public void itemAdd(RecyclerView messageRecView) {
        try {
            // Performance optimizations for smooth scrolling
            messageRecView.setItemViewCacheSize(50);
            // Removed setHasFixedSize(true) to fix lint error - incompatible with wrap_content height
            messageRecView.scrollToPosition(getItemCount() - 1);
        } catch (Exception e) {
            // Toast.makeText(mContext, "scrollToPosition" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void filterList(ArrayList<group_messageModel> filterlist) {
        groupMessageList = filterlist;
        notifyDataSetChanged();
    }

    // Method to get and print all group members
    private void printAllGroupMembers() {
//        if (grpIdKey != null && !grpIdKey.isEmpty()) {
//            Log.d("GroupMembers", "Fetching members for group: " + grpIdKey);
//
//            // Call the new Webservice method to get group members
//            Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
//                @Override
//                public void onMembersReceived(ArrayList<members> members) {
//                    Log.d("GroupMembers", "Total group members: " + members.size());
//
//                    // Print each member to logs
//                    for (int i = 0; i < members.size(); i++) {
//                        members member = members.get(i);
//                        Log.d("GroupMembers", "Member " + (i + 1) + ": " + member.toString());
//                    }
//
//                    // Show toast with member count
//                    String message = "Group has " + members.size() + " members. Check logs for details.";
//                 //   Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
//
//                    // Note: We'll need to call deleteDataForAllMembers separately with the model
//                    // since we don't have access to the model in this callback context
//                }
//            });
//        } else {
//            Log.e("GroupMembers", "Group ID is null or empty");
//          //  Toast.makeText(mContext, "Group ID not available", Toast.LENGTH_SHORT).show();
//        }
    }

    // Helper method to dismiss the delete dialog
    private void dismissDeleteDialog() {
        try {
            if (BlurHelper.dialogLayoutColor != null && BlurHelper.dialogLayoutColor.isShowing()) {
                BlurHelper.dialogLayoutColor.dismiss();
            }
        } catch (Exception e) {
            Log.e("DialogDismiss", "Error dismissing dialog: " + e.getMessage());
        }
    }

    // Helper method to stop the progress indicator for a specific message
    public void stopProgressIndicator(String modelId) {
        try {
            // Find the position of the message with this modelId
            for (int i = 0; i < groupMessageList.size(); i++) {
                group_messageModel model = groupMessageList.get(i);
                if (model != null && model.getModelId() != null && model.getModelId().equals(modelId)) {
                    // Mark the message as sent (active = 1) to stop the progress indicator
                    model.setActive(1);
                    // Notify the adapter to update this specific item
                    notifyItemChanged(i);
                    Log.d("ProgressIndicator", "Progress indicator stopped for message: " + modelId);
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("ProgressIndicator", "Error stopping progress indicator: " + e.getMessage());
        }
    }

    // Method to delete data for all group members
    private void deleteDataForAllMembers(ArrayList<members> members) {
        if (members == null || members.isEmpty()) {
            Log.d("GroupMembers", "No members to delete data for");
            return;
        }

        try {
            // Get the current model from the group message list
            // We'll need to pass the specific model from the calling context
            if (groupMessageList != null && !groupMessageList.isEmpty()) {
                // For now, use the first message as reference - this will be improved
                group_messageModel currentModel = groupMessageList.get(0);
                if (currentModel != null) {
                    Log.d("GroupMembers", "Starting delete API calls for group message: " + currentModel.getModelId());

                    // Call delete API for each member in background on main thread
                    for (int i = 0; i < members.size(); i++) {
                        members member = members.get(i);
                        final int memberIndex = i;

                        // Extract UID from member object
                        String memberUid = extractMemberUid(member);

                        if (memberUid != null && !memberUid.isEmpty()) {
                            // Use Handler to ensure smooth execution on main thread
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.d("GroupMembers", "Calling delete API for member " + (memberIndex + 1) + " with UID: " + memberUid);

                                        // Call the delete API for individual chatting
                                        Webservice.delete_chatingindivisual(mContext, currentModel.getModelId(), currentModel.getUid(), memberUid);

                                        Log.d("GroupMembers", "Delete API called successfully for member: " + memberUid);
                                    } catch (Exception e) {
                                        Log.e("GroupMembers", "Error calling delete API for member " + memberUid + ": " + e.getMessage());
                                    }
                                }
                            }, i * 100); // Add 100ms delay between each API call for smooth execution
                        } else {
                            Log.w("GroupMembers", "Member " + (i + 1) + " has invalid UID: " + memberUid);
                        }
                    }

                    Log.d("GroupMembers", "All delete API calls initiated for " + members.size() + " members");
                }
            } else {
                Log.e("GroupMembers", "Group message list is empty or null");
            }
        } catch (Exception e) {
            Log.e("GroupMembers", "Error in deleteDataForAllMembers: " + e.getMessage());
        }
    }

    // Overloaded method to delete data for all group members with specific model
    private void deleteDataForAllMembers(ArrayList<members> members, group_messageModel modelToDelete) {
        if (members == null || members.isEmpty()) {
            Log.d("GroupMembers", "No members to delete data for");
            return;
        }

        if (modelToDelete == null) {
            Log.e("GroupMembers", "Model to delete is null");
            return;
        }

        try {
            Log.d("GroupMembers", "Starting delete API calls for group message: " + modelToDelete.getModelId());

            // Call delete API for each member in background on main thread
            for (int i = 0; i < members.size(); i++) {
                members member = members.get(i);
                final int memberIndex = i;

                // Extract UID from member object
                String memberUid = extractMemberUid(member);

                if (memberUid != null && !memberUid.isEmpty()) {
                    // Use Handler to ensure smooth execution on main thread
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("GroupMembers", "Calling delete API for member " + (memberIndex + 1) + " with UID: " + memberUid);

                                // Call the delete API for individual chatting
                                Webservice.delete_chatingindivisual(mContext, modelToDelete.getModelId(), modelToDelete.getUid(), memberUid);

                                Log.d("GroupMembers", "Delete API called successfully for member: " + memberUid);
                            } catch (Exception e) {
                                Log.e("GroupMembers", "Error calling delete API for member " + memberUid + ": " + e.getMessage());
                            }
                        }
                    }, i * 100); // Add 100ms delay between each API call for smooth execution
                } else {
                    Log.w("GroupMembers", "Member " + (i + 1) + " has invalid UID: " + memberUid);
                }
            }

            Log.d("GroupMembers", "All delete API calls initiated for " + members.size() + " members");

        } catch (Exception e) {
            Log.e("GroupMembers", "Error in deleteDataForAllMembers: " + e.getMessage());
        }
    }

    // Helper method to extract UID from member object
    private String extractMemberUid(members member) {
        try {
            if (member != null) {
                // Try to get UID using reflection or direct access
                // This may need to be adjusted based on your member object structure
                if (member.getClass().getMethod("getUid") != null) {
                    return (String) member.getClass().getMethod("getUid").invoke(member);
                } else if (member.getClass().getMethod("getMemberUid") != null) {
                    return (String) member.getClass().getMethod("getMemberUid").invoke(member);
                } else if (member.getClass().getMethod("getId") != null) {
                    return (String) member.getClass().getMethod("getId").invoke(member);
                } else {
                    // Fallback: try to get UID from toString representation
                    String memberStr = member.toString();
                    Log.d("GroupMembers", "Member object: " + memberStr);

                    // Try to extract UID from string representation if it contains UID pattern
                    if (memberStr.contains("uid=") || memberStr.contains("UID=")) {
                        // Simple parsing - you may need to adjust this based on your actual format
                        String[] parts = memberStr.split("[,{}]");
                        for (String part : parts) {
                            if (part.trim().toLowerCase().contains("uid=")) {
                                String uid = part.split("=")[1].trim();
                                return uid.replace("\"", "").replace("'", "");
                            }
                        }
                    }

                    return memberStr; // Return full string if UID extraction fails
                }
            }
        } catch (Exception e) {
            Log.e("GroupMembers", "Error extracting UID from member: " + e.getMessage());
        }
        return null;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_sample_sender, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_sample_receiver, parent, false);
            return new receiverViewHolder(view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        Constant.getSfFuncion(mContext);
        String uid = Constant.getSF.getString(Constant.UID_KEY, "");

        //TODO!=REMOVE ! FROME HERE MONDAY 4MAR 2024
        //for sender
        try {
            if (groupMessageList.get(position).getUid().equals(uid)) {
                return SENDER_VIEW_TYPE;

            } else {
                return 100;
            }
        } catch (Exception e) {
            return -1;
        }


    }

    /**
     * Helper method to check if the current message has the same time display as the next message
     * @param position Current message position
     * @return true if current message has same time display as next message, false otherwise
     */
    private boolean hasSameTimeAsNext(int position) {
        if (position >= groupMessageList.size() - 1) {
            return false; // Last message, no next message to compare
        }

        group_messageModel currentMessage = groupMessageList.get(position);
        group_messageModel nextMessage = groupMessageList.get(position + 1);

        // Add null checks
        if (currentMessage == null || nextMessage == null) {
            return false;
        }

        // Compare the actual time strings that are displayed to users
        String currentTime = currentMessage.getTime();
        String nextTime = nextMessage.getTime();

        if (currentTime == null || nextTime == null) {
            return false;
        }

        // Check if time strings are the same
        boolean isSameTime = currentTime.equals(nextTime);

        return isSameTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final group_messageModel model = groupMessageList.get(position);


        // Single quality setting for all images - 75% quality
        requestOptions = requestOptions.dontAnimate();
        requestOptions = requestOptions
                .format(DecodeFormat.PREFER_ARGB_8888) // High quality format
                .encodeQuality(75) // Single 75% quality for all images
                .override((int) (300 * mContext.getResources().getDisplayMetrics().density));


        database = FirebaseDatabase.getInstance();

        if (groupMessageList.size() == 0) {

            if (valuable.getVisibility() == View.GONE) {
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(2000);
                valuable.startAnimation(fadeIn);
                valuable.setVisibility(View.VISIBLE);
            }

        } else {
            if (valuable.getVisibility() == View.VISIBLE) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(2000);
                valuable.startAnimation(fadeOut);
                valuable.setVisibility(View.GONE);
            }
        }

        try {
            if (holder.getClass() == senderViewHolder.class) {


                int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    // Dark mode is active
                    Constant.getSfFuncion(mContext);
                    String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                    ColorStateList tintList;

                    try {
                        if (themColor.equals("#ff0080")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#00A3E9")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else if (themColor.equals("#7adf2a")) {

                            tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#ec0001")) {

                            tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#16f3ff")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else if (themColor.equals("#FF8A00")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#7F7F7F")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else if (themColor.equals("#D9B845")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else if (themColor.equals("#346667")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else if (themColor.equals("#9846D9")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        } else if (themColor.equals("#A81010")) {
                            tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);

                        } else {
                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                            ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                        }
                    } catch (Exception ignored) {
                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                        ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList);
                        ((senderViewHolder) holder).richBox.setBackgroundTintList(tintList);


                    }


                } else {
                    tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                    ((senderViewHolder) holder).MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value


                }


                if (position == groupMessageList.size() - 1) {


                    if (isLastItemVisible) {
                        ((senderViewHolder) holder).viewnew.setIndeterminate(true);
                    } else {
                        ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                    }


                } else {
                    ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                }


                // Check if this message has same time as next message
                boolean hasSameTime = hasSameTimeAsNext(position);

                // Always show timing
                ((senderViewHolder) holder).sendTime.setVisibility(View.VISIBLE);
                ((senderViewHolder) holder).sendTime.setText(model.getTime());

                try {
                    if (model.getCurrentDate().equals(Constant.getCurrentDate())) {
                        ((senderViewHolder) holder).dateTxt.setText("Today");
                    } else if (model.getCurrentDate().equals(Constant.getYesterdayDate())) {
                        ((senderViewHolder) holder).dateTxt.setText("Yesterday");
                    } else {
                        ((senderViewHolder) holder).dateTxt.setText(model.getCurrentDate());
                    }


                    if (((senderViewHolder) holder).dateTxt.getText().toString().contains(":")) {
                        ((senderViewHolder) holder).datelyt.setVisibility(View.GONE);
                    } else {
                        ((senderViewHolder) holder).datelyt.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                }

                // Reduce spacing between messages with same time
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ((senderViewHolder) holder).itemView.getLayoutParams();
                if (layoutParams != null) {
                    if (hasSameTime) {
                        // Reduce bottom margin for messages with same time
                        layoutParams.bottomMargin = 2; // Reduced spacing
                    } else {
                        // Normal spacing for different times
                        layoutParams.bottomMargin = 8; // Normal spacing
                    }
                    ((senderViewHolder) holder).itemView.setLayoutParams(layoutParams);
                }

                try {

                    Constant.getSfFuncion(mContext);
                    themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                    tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                    //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                    try {
                        if (themColor.equals("#ff0080")) {
                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));

                        } else if (themColor.equals("#00A3E9")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#7adf2a")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#ec0001")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#16f3ff")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#FF8A00")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#7F7F7F")) {


                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));

                        } else if (themColor.equals("#D9B845")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#346667")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#9846D9")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else if (themColor.equals("#A81010")) {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));


                        } else {

                            ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                            ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));

                        }
                    } catch (Exception ignored) {

                        ((senderViewHolder) holder).viewnew.setTrackColor(Color.parseColor(themColor));
                        ((senderViewHolder) holder).readMore.setTextColor(Color.parseColor(themColor));
                    }


                } catch (Exception ignored) {
                }

                if (model.getDataType().equals(Constant.img)) {

                    Log.d("TAG444", "com");
                    Log.d("Mdid", model.getModelId());
                    ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                    ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);

                    ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                    ((senderViewHolder) holder).miceContainer.setVisibility(View.GONE);
                    if (!model.getCaption().equals("")) {
                        ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).captionText.setText(model.getCaption());

                    } else {
                        ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                    }


                    if (model.getSelectionCount() != null) {
                        if (model.getSelectionCount().equals("1")) {

                            ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                            ((senderViewHolder) holder).senderImg.setVisibility(View.VISIBLE);


                            File customFolder2;
                            String exactPath2;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder2 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                exactPath2 = customFolder2.getAbsolutePath();
                            } else {
                                customFolder2 = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                                exactPath2 = customFolder2.getAbsolutePath();
                            }
                            if (doesFileExist(exactPath2 + "/" + model.getFileName())) {
                                Log.d("TAG", "statusxascac: " + "existingimage " + exactPath2 + "/" + model.getFileName());
                                Log.d("loadImageIntoViewTT", "getImageWidth: " + model.getImageWidth());
                                Log.d("loadImageIntoViewTT", "getImageHeight: " + model.getImageHeight());
                                Log.d("loadImageIntoViewTT", "getAspectRatio: " + model.getAspectRatio());

                                File imageFile = new File(exactPath2 + "/" + model.getFileName());


                                String imageSource = exactPath2 + "/" + model.getFileName();
                                ImageView targetImageView = ((senderViewHolder) holder).senderImg;
                                ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                // ✅ Use new blur optimization to prevent black images and pixelation

                                BlurImageOptimizer.loadImageWithSafeBlur(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, model, ((senderViewHolder) holder).videoicon);


                                Constant.loadImageIntoViewGroup(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, ((senderViewHolder) holder).videoicon);
//


                                ((senderViewHolder) holder).downlaod.setVisibility(View.GONE);
                                ((senderViewHolder) holder).progressBarImageview.setVisibility(View.GONE);
                            } else {
                                Log.d("TAG", "status: " + "notimage");


                                String imageSource = model.getDocument();
                                ImageView targetImageView = ((senderViewHolder) holder).senderImg;
                                ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                BlurImageOptimizer.loadImageWithSafeBlur(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, model, ((senderViewHolder) holder).videoicon);


                                Constant.loadImageIntoViewGroup(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, ((senderViewHolder) holder).videoicon);


                                ((senderViewHolder) holder).downlaod.setVisibility(View.VISIBLE);
                                ((senderViewHolder) holder).progressBarImageview.setVisibility(View.GONE);

                            }

                        } else
                        if (model.getSelectionCount().equals("2")) {


                            ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                            ((senderViewHolder) holder).overlayTextImg.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img2.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img4.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img4Lyt.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img1.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img3.setVisibility(View.VISIBLE);

                            Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                    + ", selectionCount=" + model.getSelectionCount()
                                    + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                            bindSelectionBunchImages((senderViewHolder) holder, model, requestOptions, position, true);


                            // ================= For selectionCount=2: Both images 125dp x 251.5dp =================
                            float heightInDp = 251.5f;
                            float widthInDp2 = 125f;

                            int heightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    heightInDp,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            int widthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    widthInDp2,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            float cornerRadius = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    20,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            // ================= For img1 (Left side - left corners rounded) =================
                            ViewGroup.LayoutParams params1 = ((senderViewHolder) holder).img1.getLayoutParams();
                            if (params1 != null) {
                                params1.height = heightInPx;
                                params1.width = widthInPx;
                                ((senderViewHolder) holder).img1.setLayoutParams(params1);
                            }

                            ShapeAppearanceModel shapeModel1 = ((senderViewHolder) holder).img1.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img1.setShapeAppearanceModel(shapeModel1);

                            // ================= For img3 (Right side - right corners rounded) =================
                            ViewGroup.LayoutParams params3 = ((senderViewHolder) holder).img3.getLayoutParams();
                            if (params3 != null) {
                                params3.height = heightInPx;
                                params3.width = widthInPx;
                                ((senderViewHolder) holder).img3.setLayoutParams(params3);
                            }

                            ShapeAppearanceModel shapeModel3 = ((senderViewHolder) holder).img3.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .build();

                            ((senderViewHolder) holder).img3.setShapeAppearanceModel(shapeModel3);

                            Log.d("SelectionBunch", "Set both images for selectionCount=2: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");



                        } else if (model.getSelectionCount().equals("3")) {

                            ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                            ((senderViewHolder) holder).overlayTextImg.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img2.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img4.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img4Lyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img1.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img3.setVisibility(View.VISIBLE);

                            // ================= For img1 (Height: 251.5dp, Width: 125dp) =================
                            float img1HeightInDp = 251.5f;
                            float img1WidthInDp = 125f;
                            int img1HeightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img1HeightInDp,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            int img1WidthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img1WidthInDp,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            ViewGroup.LayoutParams params1 = ((senderViewHolder) holder).img1.getLayoutParams();
                            if (params1 != null) {
                                params1.height = img1HeightInPx;
                                params1.width = img1WidthInPx;
                                ((senderViewHolder) holder).img1.setLayoutParams(params1);
                                Log.d("SelectionBunch", "Set img1 dimensions: " + img1WidthInPx + "x" + img1HeightInPx + "px (" + img1WidthInDp + "x" + img1HeightInDp + "dp)");
                            }

                            float cornerRadius = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    20,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            ShapeAppearanceModel shapeModel1 = ((senderViewHolder) holder).img1.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img1.setShapeAppearanceModel(shapeModel1);

                            // ================= For img3 (Height: 125dp, Width: 125dp) =================
                            float img3HeightInDp = 125f;
                            float img3WidthInDp = 125f;
                            int img3HeightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img3HeightInDp,
                                    ((senderViewHolder) holder).img3.getResources().getDisplayMetrics()
                            );

                            int img3WidthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img3WidthInDp,
                                    ((senderViewHolder) holder).img3.getResources().getDisplayMetrics()
                            );

                            ViewGroup.LayoutParams params3 = ((senderViewHolder) holder).img3.getLayoutParams();
                            if (params3 != null) {
                                params3.height = img3HeightInPx;
                                params3.width = img3WidthInPx;
                                ((senderViewHolder) holder).img3.setLayoutParams(params3);
                                Log.d("SelectionBunch", "Set img3 dimensions: " + img3WidthInPx + "x" + img3HeightInPx + "px (" + img3WidthInDp + "x" + img3HeightInDp + "dp)");
                            }

                            ShapeAppearanceModel shapeModel3 = ((senderViewHolder) holder).img3.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img3.setShapeAppearanceModel(shapeModel3);

                            // ================= For img4 (Height: 125dp, Width: 125dp) =================
                            float img4HeightInDp = 125f;
                            float img4WidthInDp = 125f;
                            int img4HeightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img4HeightInDp,
                                    ((senderViewHolder) holder).img4.getResources().getDisplayMetrics()
                            );

                            int img4WidthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    img4WidthInDp,
                                    ((senderViewHolder) holder).img4.getResources().getDisplayMetrics()
                            );

                            ViewGroup.LayoutParams params4 = ((senderViewHolder) holder).img4.getLayoutParams();
                            if (params4 != null) {
                                params4.height = img4HeightInPx;
                                params4.width = img4WidthInPx;
                                ((senderViewHolder) holder).img4.setLayoutParams(params4);
                                Log.d("SelectionBunch", "Set img4 dimensions: " + img4WidthInPx + "x" + img4HeightInPx + "px (" + img4WidthInDp + "x" + img4HeightInDp + "dp)");
                            }

                            ShapeAppearanceModel shapeModel4 = ((senderViewHolder) holder).img4.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img4.setShapeAppearanceModel(shapeModel4);

                            Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                    + ", selectionCount=" + model.getSelectionCount()
                                    + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                            bindSelectionBunchImages((senderViewHolder) holder, model, requestOptions, position, true);

                        } else
                        if (model.getSelectionCount().equals("4")) {

                            ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);

                            ((senderViewHolder) holder).img2.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img4.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img4Lyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img1.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img3.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).overlayTextImg.setVisibility(View.GONE);

                            // ================= For selectionCount=4: All images 125dp x 125.5dp =================
                            float heightInDp = 125.5f;
                            float widthInDp4 = 125f;

                            int heightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    heightInDp,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            int widthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    widthInDp4,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            float cornerRadius = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    20,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            // ================= For img1 (Top-left corner rounded) =================
                            ViewGroup.LayoutParams params1 = ((senderViewHolder) holder).img1.getLayoutParams();
                            if (params1 != null) {
                                params1.height = heightInPx;
                                params1.width = widthInPx;
                                ((senderViewHolder) holder).img1.setLayoutParams(params1);
                            }

                            ShapeAppearanceModel shapeModel1 = ((senderViewHolder) holder).img1.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img1.setShapeAppearanceModel(shapeModel1);

                            // ================= For img2 (Bottom-left corner rounded) =================
                            Log.d("SelectionBunch", "=== IMG2 SETUP START for selectionCount=4 ===");
                            Log.d("SelectionBunch", "img2 initial state - visibility: " + ((senderViewHolder) holder).img2.getVisibility() +
                                    ", width: " + ((senderViewHolder) holder).img2.getWidth() +
                                    ", height: " + ((senderViewHolder) holder).img2.getHeight());

                            ViewGroup.LayoutParams params2 = ((senderViewHolder) holder).img2.getLayoutParams();
                            if (params2 != null) {
                                Log.d("SelectionBunch", "img2 original layoutParams: " + params2.width + "x" + params2.height);
                                params2.height = heightInPx;
                                params2.width = widthInPx;
                                ((senderViewHolder) holder).img2.setLayoutParams(params2);

                                // Force layout pass to apply the new dimensions
                                ((senderViewHolder) holder).img2.requestLayout();
                                ((senderViewHolder) holder).img2.invalidate();

                                Log.d("SelectionBunch", "Set img2 dimensions for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125.5dp)");
                                Log.d("SelectionBunch", "img2 after setLayoutParams - width: " + ((senderViewHolder) holder).img2.getWidth() +
                                        ", height: " + ((senderViewHolder) holder).img2.getHeight());
                            } else {
                                Log.e("SelectionBunch", "img2 layoutParams is NULL!");
                            }

                            ShapeAppearanceModel shapeModel2 = ((senderViewHolder) holder).img2.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img2.setShapeAppearanceModel(shapeModel2);

                            // ================= For img3 (Top-right corner rounded) =================
                            ViewGroup.LayoutParams params3 = ((senderViewHolder) holder).img3.getLayoutParams();
                            if (params3 != null) {
                                params3.height = heightInPx;
                                params3.width = widthInPx;
                                ((senderViewHolder) holder).img3.setLayoutParams(params3);
                            }

                            ShapeAppearanceModel shapeModel3 = ((senderViewHolder) holder).img3.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img3.setShapeAppearanceModel(shapeModel3);

                            // ================= For img4 (Bottom-right corner rounded) =================
                            ViewGroup.LayoutParams params4 = ((senderViewHolder) holder).img4.getLayoutParams();
                            if (params4 != null) {
                                params4.height = heightInPx;
                                params4.width = widthInPx;
                                ((senderViewHolder) holder).img4.setLayoutParams(params4);
                            }

                            ShapeAppearanceModel shapeModel4 = ((senderViewHolder) holder).img4.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .build();

                            ((senderViewHolder) holder).img4.setShapeAppearanceModel(shapeModel4);

                            Log.d("SelectionBunch", "Set all images for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");

                            Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                    + ", selectionCount=" + model.getSelectionCount()
                                    + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                            bindSelectionBunchImages((senderViewHolder) holder, model, requestOptions, position, true);

                        } else {



                            ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                            ((senderViewHolder) holder).img2.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img4.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img4Lyt.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img1.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).img3.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).overlayTextImg.setVisibility(View.VISIBLE);


                            int a = Integer.parseInt(model.getSelectionCount());

                            ((senderViewHolder) holder).overlayTextImg.setText("+ "+String.valueOf(a-3));

                            // ================= For selectionCount=4: All images 125dp x 125.5dp =================
                            float heightInDp = 125.5f;
                            float widthInDp4 = 125f;

                            int heightInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    heightInDp,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            int widthInPx = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    widthInDp4,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            float cornerRadius = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    20,
                                    ((senderViewHolder) holder).img1.getResources().getDisplayMetrics()
                            );

                            // ================= For img1 (Top-left corner rounded) =================
                            ViewGroup.LayoutParams params1 = ((senderViewHolder) holder).img1.getLayoutParams();
                            if (params1 != null) {
                                params1.height = heightInPx;
                                params1.width = widthInPx;
                                ((senderViewHolder) holder).img1.setLayoutParams(params1);
                            }

                            ShapeAppearanceModel shapeModel1 = ((senderViewHolder) holder).img1.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img1.setShapeAppearanceModel(shapeModel1);

                            // ================= For img2 (Bottom-left corner rounded) =================
                            Log.d("SelectionBunch", "=== IMG2 SETUP START for selectionCount=4 ===");
                            Log.d("SelectionBunch", "img2 initial state - visibility: " + ((senderViewHolder) holder).img2.getVisibility() +
                                    ", width: " + ((senderViewHolder) holder).img2.getWidth() +
                                    ", height: " + ((senderViewHolder) holder).img2.getHeight());

                            ViewGroup.LayoutParams params2 = ((senderViewHolder) holder).img2.getLayoutParams();
                            if (params2 != null) {
                                Log.d("SelectionBunch", "img2 original layoutParams: " + params2.width + "x" + params2.height);
                                params2.height = heightInPx;
                                params2.width = widthInPx;
                                ((senderViewHolder) holder).img2.setLayoutParams(params2);

                                // Force layout pass to apply the new dimensions
                                ((senderViewHolder) holder).img2.requestLayout();
                                ((senderViewHolder) holder).img2.invalidate();

                                Log.d("SelectionBunch", "Set img2 dimensions for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125.5dp)");
                                Log.d("SelectionBunch", "img2 after setLayoutParams - width: " + ((senderViewHolder) holder).img2.getWidth() +
                                        ", height: " + ((senderViewHolder) holder).img2.getHeight());
                            } else {
                                Log.e("SelectionBunch", "img2 layoutParams is NULL!");
                            }

                            ShapeAppearanceModel shapeModel2 = ((senderViewHolder) holder).img2.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img2.setShapeAppearanceModel(shapeModel2);

                            // ================= For img3 (Top-right corner rounded) =================
                            ViewGroup.LayoutParams params3 = ((senderViewHolder) holder).img3.getLayoutParams();
                            if (params3 != null) {
                                params3.height = heightInPx;
                                params3.width = widthInPx;
                                ((senderViewHolder) holder).img3.setLayoutParams(params3);
                            }

                            ShapeAppearanceModel shapeModel3 = ((senderViewHolder) holder).img3.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                    .build();

                            ((senderViewHolder) holder).img3.setShapeAppearanceModel(shapeModel3);

                            // ================= For img4 (Bottom-right corner rounded) =================
                            ViewGroup.LayoutParams params4 = ((senderViewHolder) holder).img4.getLayoutParams();
                            if (params4 != null) {
                                params4.height = heightInPx;
                                params4.width = widthInPx;
                                ((senderViewHolder) holder).img4.setLayoutParams(params4);
                            }

                            ShapeAppearanceModel shapeModel4 = ((senderViewHolder) holder).img4.getShapeAppearanceModel()
                                    .toBuilder()
                                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                    .build();

                            ((senderViewHolder) holder).img4.setShapeAppearanceModel(shapeModel4);

                            Log.d("SelectionBunch", "Set all images for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");

                            Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                    + ", selectionCount=" + model.getSelectionCount()
                                    + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                            bindSelectionBunchImages((senderViewHolder) holder, model, requestOptions, position, true);
                        }
                    } else {


                        ((senderViewHolder) holder).senderImg.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    }


                    // Open document on tapping any document UI element (more reliable than itemView)
                    View.OnClickListener openDocClick = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File customFolder;
                            String exactPath = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                exactPath = customFolder.getAbsolutePath();
                            } else {
                                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                                exactPath = customFolder.getAbsolutePath();
                            }

                            if (doesFileExist(exactPath + "/" + model.getFileName())) {
                                Intent intent = new Intent(mContext, show_document_screen.class);
                                intent.putExtra("documentKey", model.getFileName());
                                intent.putExtra("nameKey", model.getFileName());
                                intent.putExtra("sizeKey", model.getDocSize());
                                intent.putExtra("extensionKey", model.getExtension());
                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            } else {
                                Intent intent = new Intent(mContext, show_document_screen.class);
                                intent.putExtra("documentKey", model.getDocument());
                                intent.putExtra("nameKey", model.getFileName());
                                intent.putExtra("sizeKey", model.getDocSize());
                                intent.putExtra("extensionKey", model.getExtension());
                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            }
                        }
                    };

                    ((senderViewHolder) holder).docLyt.setOnClickListener(openDocClick);
                    ((senderViewHolder) holder).docFileIcon.setOnClickListener(openDocClick);
                    ((senderViewHolder) holder).pdfcard.setOnClickListener(openDocClick);
                    ((senderViewHolder) holder).pdfPreview.setOnClickListener(openDocClick);



                    ((senderViewHolder) holder).senderImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            File customFolder;
                            String exactPath = null;
                            //android 10
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                exactPath = customFolder.getAbsolutePath();
                                Log.d("TAG", "exactPath: " + exactPath + "/" + model.getFileName());
                            } else {
                                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                                exactPath = customFolder.getAbsolutePath();

                            }

                            if (doesFileExist(exactPath + "/" + model.getFileName())) {

                                Intent intent = new Intent(mContext, show_image_Screen.class);
                                intent.putExtra("imageKey", model.getFileName());
                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            } else {
//                                Intent intent = new Intent(mContext, show_image_Screen.class);
//                                intent.putExtra("imageKey", model.getDocument());
//                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
//                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            }


                        }
                    });

                    ((senderViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            File customFolder;
                            String exactPath = null;
                            //android 10
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                exactPath = customFolder.getAbsolutePath();
                                Log.d("TAG", "exactPath: " + exactPath + "/" + model.getFileName());
                            } else {
                                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                                exactPath = customFolder.getAbsolutePath();

                            }




                            if(model.getSelectionBunch().size() > 1){
                                // Handle multiple images - create imageList with local storage check
                                ArrayList<String> imageList = new ArrayList<>();
                                int currentPosition = 0;

                                Log.d("ChatAdapter", "Processing " + model.getSelectionBunch().size() + " images for multiple display");
                                Log.d("ChatAdapter", "Local storage path: " + exactPath);

                                // Get all files in the directory
                                File directory = new File(exactPath);
                                String[] allFiles = null;
                                if (directory.exists() && directory.isDirectory()) {
                                    allFiles = directory.list();
                                    if (allFiles != null) {
                                        Log.d("ChatAdapter", "All files in directory: " + java.util.Arrays.toString(allFiles));
                                    }
                                }

                                // Create a list of available local files with "img_" prefix
                                ArrayList<String> availableLocalFiles = new ArrayList<>();
                                if (allFiles != null) {
                                    for (String file : allFiles) {
                                        if (file.startsWith("img_")) {
                                            availableLocalFiles.add(file);
                                        }
                                    }
                                }
                                Log.d("ChatAdapter", "Available local files with 'img_' prefix: " + availableLocalFiles);

                                // For now, use network URLs to ensure correct image order
                                // This prevents using wrong local files from different image sets
                                Log.d("ChatAdapter", "Using network URLs to ensure correct image order and prevent wrong local file usage");

                                for (int i = 0; i < model.getSelectionBunch().size(); i++) {
                                    selectionBunchModel bunchModel = model.getSelectionBunch().get(i);
                                    String fileName = bunchModel.getFileName();
                                    String networkUrl = bunchModel.getImgUrl();

                                    Log.d("ChatAdapter", "Image " + i + " - fileName: " + fileName + ", networkUrl: " + networkUrl);

                                    // Create bundle with both fileName and URL
                                    String bundleData = fileName + "|" + networkUrl;

                                    // Use network image to ensure correct order and prevent wrong local file usage
                                    if (networkUrl != null && !networkUrl.isEmpty()) {
                                        imageList.add(bundleData);
                                        Log.d("ChatAdapter", "✅ Added bundle data " + i + ": " + bundleData);
                                    } else {
                                        // Use document as fallback
                                        if (model.getDocument() != null && !model.getDocument().isEmpty()) {
                                            imageList.add(model.getDocument());
                                            Log.d("ChatAdapter", "✅ Added document fallback " + i + ": " + model.getDocument());
                                        } else {
                                            Log.w("ChatAdapter", "❌ No valid image source found for image " + i);
                                        }
                                    }
                                }

                                Log.d("ChatAdapter", "Final image list size: " + imageList.size() + " (all network URLs for correct order)");

                                if (!imageList.isEmpty()) {
                                    // Use dialog with filename-based positioning
                                    Log.d("ChatAdapter", "=== OPENING MULTIPLE IMAGE DIALOG ===");
                                    Log.d("ChatAdapter", "Target filename: " + model.getFileName());
                                    Log.d("ChatAdapter", "Image list size: " + imageList.size());
                                    Log.d("ChatAdapter", "Current position: " + currentPosition);

                                    // Log the complete image list being passed
                                    Log.d("ChatAdapter", "=== IMAGE LIST BEING PASSED TO DIALOG ===");
                                    for (int i = 0; i < imageList.size(); i++) {
                                        Log.d("ChatAdapter", "Image " + i + ": " + imageList.get(i));
                                    }

                                    MultipleImageDialogHelper.showDialogWithFilename(
                                            activity.getSupportFragmentManager(),
                                            imageList,
                                            model.getFileName(), // Use filename for precise positioning
                                            Constant.senderViewHolder
                                    );
                                } else {
                                    Log.w("ChatAdapter", "No images found for multiple image display");
                                }
                            }else{

                                // Fallback to single image flow (use local if available)
                                if (doesFileExist(exactPath + "/" + model.getFileName())) {
                                    Intent intent = new Intent(mContext, show_image_Screen.class);
                                    intent.putExtra("imageKey", model.getFileName());
                                    intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                } else if (model.getDocument() != null && !model.getDocument().isEmpty()) {
                                    Intent intent = new Intent(mContext, show_image_Screen.class);
                                    intent.putExtra("imageKey", model.getDocument());
                                    intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                }
                            }



                        }
                    });

                    ((senderViewHolder) holder).downlaod.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startSenderImageDownloadWithProgress(holder, model);


                          ///  saveImageToGallery(model);
                        }
                    });
                    ((senderViewHolder) holder).senderImg.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            float touchX = location[0];
                            float touchY = location[1];


                            BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                            BlurHelper.dialogLayoutColor.show();
                            RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                            // Use boundary-aware positioning
                            BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                            TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                            TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                            TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                            TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                            TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                            TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                            TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                            TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                            TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                            TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                            RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                            RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                            RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                            ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                            ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                            ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                            ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                            ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                            LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                            AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                            LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                            LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                            LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                            copy.setVisibility(View.GONE);
                            LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                            LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                            LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                            View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                            View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                            View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                            View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                            CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                            TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                            FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                            LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                            LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                            CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                            LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                            TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                            LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                            CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                            CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                            ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                            TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                            ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                            TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                            RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                            FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                            ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                            TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                            ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                            RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                            ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                            TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                            FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                            ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                            CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                            LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                            TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                            TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                            TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);


                            RelativeLayout senderImgBunchLyt;
                            ShapeableImageView img1, img3, img2, img4;
                            FrameLayout img4Lyt;
                            TextView overlayTextImg;
                            FloatingActionButton downlaodImgBunch;
                            TextView downloadPercentageImageSenderBunch;

                            senderImgBunchLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgBunchLyt);
                            img1 = BlurHelper.dialogLayoutColor.findViewById(R.id.img1);
                            img2 = BlurHelper.dialogLayoutColor.findViewById(R.id.img2);
                            img3 = BlurHelper.dialogLayoutColor.findViewById(R.id.img3);
                            img4 = BlurHelper.dialogLayoutColor.findViewById(R.id.img4);
                            img4Lyt = BlurHelper.dialogLayoutColor.findViewById(R.id.img4Lyt);
                            overlayTextImg = BlurHelper.dialogLayoutColor.findViewById(R.id.overlayTextImg);
                            downlaodImgBunch = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodImgBunch);
                            downloadPercentageImageSenderBunch = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageImageSenderBunch);
                            AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                            LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                            LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                            int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                // Dark mode is active
                                Constant.getSfFuncion(mContext);
                                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                ColorStateList tintList;

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#00A3E9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#7adf2a")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#ec0001")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#16f3ff")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#FF8A00")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#7F7F7F")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#D9B845")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#346667")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#9846D9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#A81010")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }
                                } catch (Exception ignored) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    MainSenderBox.setBackgroundTintList(tintList);
                                    richBox.setBackgroundTintList(tintList);


                                }


                            } else {
                                tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                            }

                            // todo theme
                            try {

                                Constant.getSfFuncion(mContext);
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#00A3E9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7adf2a")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#ec0001")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#16f3ff")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#FF8A00")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7F7F7F")) {


                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    } else if (themColor.equals("#D9B845")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#346667")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#9846D9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#A81010")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    }
                                } catch (Exception ignored) {

                                    viewnew.setBackgroundTintList(tintList);
                                    readMore.setTextColor(Color.parseColor(themColor));

                                }


                            } catch (Exception ignored) {
                            }
                            Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                            cardview.startAnimation(animationdd);
                            sendTime.setText(model.getTime());


                            // main code from here


                            Log.d("TAG444", "com");
                            Log.d("TAG444", "com");
                            Log.d("Mdid", model.getModelId());
                            readMore.setVisibility(View.GONE);
                            richLinkViewLyt.setVisibility(View.GONE);
                            sendMessage.setVisibility(View.GONE);


                            if (model.getSelectionCount() != null) {
                                if (model.getSelectionCount().equals("1")) {
                                    senderImgLyt.setVisibility(View.VISIBLE);
                                    senderImgBunchLyt.setVisibility(View.GONE);
                                    senderImg.setVisibility(View.VISIBLE);


                                    File customFolder2;
                                    String exactPath2;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        customFolder2 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                        exactPath2 = customFolder2.getAbsolutePath();
                                    } else {
                                        customFolder2 = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                                        exactPath2 = customFolder2.getAbsolutePath();
                                    }
                                    if (doesFileExist(exactPath2 + "/" + model.getFileName())) {
                                        Log.d("TAG", "statusxascac: " + "existingimage " + exactPath2 + "/" + model.getFileName());
                                        Log.d("loadImageIntoViewTT", "getImageWidth: " + model.getImageWidth());
                                        Log.d("loadImageIntoViewTT", "getImageHeight: " + model.getImageHeight());
                                        Log.d("loadImageIntoViewTT", "getAspectRatio: " + model.getAspectRatio());

                                        File imageFile = new File(exactPath2 + "/" + model.getFileName());


                                        String imageSource = exactPath2 + "/" + model.getFileName();
                                        ImageView targetImageView = senderImg;
                                        ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                        // ✅ Use new blur optimization to prevent black images and pixelation

                                        BlurImageOptimizer.loadImageWithSafeBlur(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, model, videoicon);


                                        Constant.loadImageIntoViewGroup(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);
//


                                        downlaod.setVisibility(View.GONE);
                                        progressBarImageview.setVisibility(View.GONE);
                                    } else {
                                        Log.d("TAG", "status: " + "notimage");


                                        String imageSource = model.getDocument();
                                        ImageView targetImageView = senderImg;
                                        ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                        BlurImageOptimizer.loadImageWithSafeBlur(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, model, videoicon);


                                        Constant.loadImageIntoViewGroup(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);


                                        downlaod.setVisibility(View.VISIBLE);
                                        progressBarImageview.setVisibility(View.GONE);

                                    }

                                } else
                                if (model.getSelectionCount().equals("2")) {

                                    senderImgLyt.setVisibility(View.GONE);
                                    senderImgBunchLyt.setVisibility(View.VISIBLE);
                                    senderImg.setVisibility(View.GONE);
                                    overlayTextImg.setVisibility(View.GONE);
                                    img2.setVisibility(View.GONE);
                                    img4.setVisibility(View.GONE);
                                    img4Lyt.setVisibility(View.GONE);
                                    img1.setVisibility(View.VISIBLE);
                                    img3.setVisibility(View.VISIBLE);

                                    Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                            + ", selectionCount=" + model.getSelectionCount()
                                            + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                                    bindSelectionBunchImagesSenderLong( model, requestOptions, position, true,img1,img2,img3,img4,img4Lyt,videoicon,overlayTextImg);


                                    // ================= For selectionCount=2: Both images 125dp x 251.5dp =================
                                    float heightInDp = 251.5f;
                                    float widthInDp2 = 125f;

                                    int heightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            heightInDp,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    int widthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            widthInDp2,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    float cornerRadius = TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            20,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    // ================= For img1 (Left side - left corners rounded) =================
                                    ViewGroup.LayoutParams params1 = img1.getLayoutParams();
                                    if (params1 != null) {
                                        params1.height = heightInPx;
                                        params1.width = widthInPx;
                                        img1.setLayoutParams(params1);
                                    }

                                    ShapeAppearanceModel shapeModel1 = img1.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img1.setShapeAppearanceModel(shapeModel1);

                                    // ================= For img3 (Right side - right corners rounded) =================
                                    ViewGroup.LayoutParams params3 = img3.getLayoutParams();
                                    if (params3 != null) {
                                        params3.height = heightInPx;
                                        params3.width = widthInPx;
                                        img3.setLayoutParams(params3);
                                    }

                                    ShapeAppearanceModel shapeModel3 = img3.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .build();

                                    img3.setShapeAppearanceModel(shapeModel3);

                                    Log.d("SelectionBunch", "Set both images for selectionCount=2: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");



                                } else if (model.getSelectionCount().equals("3")) {
                                    senderImgLyt.setVisibility(View.GONE);
                                    senderImgBunchLyt.setVisibility(View.VISIBLE);
                                    senderImg.setVisibility(View.GONE);
                                    overlayTextImg.setVisibility(View.GONE);
                                    img2.setVisibility(View.GONE);
                                    img4.setVisibility(View.VISIBLE);
                                    img4Lyt.setVisibility(View.VISIBLE);
                                    img1.setVisibility(View.VISIBLE);
                                    img3.setVisibility(View.VISIBLE);

                                    // ================= For img1 (Height: 251.5dp, Width: 125dp) =================
                                    float img1HeightInDp = 251.5f;
                                    float img1WidthInDp = 125f;
                                    int img1HeightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img1HeightInDp,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    int img1WidthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img1WidthInDp,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    ViewGroup.LayoutParams params1 = img1.getLayoutParams();
                                    if (params1 != null) {
                                        params1.height = img1HeightInPx;
                                        params1.width = img1WidthInPx;
                                        img1.setLayoutParams(params1);
                                        Log.d("SelectionBunch", "Set img1 dimensions: " + img1WidthInPx + "x" + img1HeightInPx + "px (" + img1WidthInDp + "x" + img1HeightInDp + "dp)");
                                    }

                                    float cornerRadius = TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            20,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    ShapeAppearanceModel shapeModel1 = img1.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img1.setShapeAppearanceModel(shapeModel1);

                                    // ================= For img3 (Height: 125dp, Width: 125dp) =================
                                    float img3HeightInDp = 125f;
                                    float img3WidthInDp = 125f;
                                    int img3HeightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img3HeightInDp,
                                            img3.getResources().getDisplayMetrics()
                                    );

                                    int img3WidthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img3WidthInDp,
                                            img3.getResources().getDisplayMetrics()
                                    );

                                    ViewGroup.LayoutParams params3 = img3.getLayoutParams();
                                    if (params3 != null) {
                                        params3.height = img3HeightInPx;
                                        params3.width = img3WidthInPx;
                                        img3.setLayoutParams(params3);
                                        Log.d("SelectionBunch", "Set img3 dimensions: " + img3WidthInPx + "x" + img3HeightInPx + "px (" + img3WidthInDp + "x" + img3HeightInDp + "dp)");
                                    }

                                    ShapeAppearanceModel shapeModel3 = img3.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img3.setShapeAppearanceModel(shapeModel3);

                                    // ================= For img4 (Height: 125dp, Width: 125dp) =================
                                    float img4HeightInDp = 125f;
                                    float img4WidthInDp = 125f;
                                    int img4HeightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img4HeightInDp,
                                            img4.getResources().getDisplayMetrics()
                                    );

                                    int img4WidthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            img4WidthInDp,
                                            img4.getResources().getDisplayMetrics()
                                    );

                                    ViewGroup.LayoutParams params4 = img4.getLayoutParams();
                                    if (params4 != null) {
                                        params4.height = img4HeightInPx;
                                        params4.width = img4WidthInPx;
                                        img4.setLayoutParams(params4);
                                        Log.d("SelectionBunch", "Set img4 dimensions: " + img4WidthInPx + "x" + img4HeightInPx + "px (" + img4WidthInDp + "x" + img4HeightInDp + "dp)");
                                    }

                                    ShapeAppearanceModel shapeModel4 = img4.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img4.setShapeAppearanceModel(shapeModel4);

                                    Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                            + ", selectionCount=" + model.getSelectionCount()
                                            + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                                    bindSelectionBunchImagesSenderLong( model, requestOptions, position, true,img1,img2,img3,img4,img4Lyt,videoicon,overlayTextImg);

                                } else
                                if (model.getSelectionCount().equals("4")) {
                                    senderImgLyt.setVisibility(View.GONE);
                                    senderImgBunchLyt.setVisibility(View.VISIBLE);
                                    senderImg.setVisibility(View.GONE);

                                    img2.setVisibility(View.VISIBLE);
                                    img4.setVisibility(View.VISIBLE);
                                    img4Lyt.setVisibility(View.VISIBLE);
                                    img1.setVisibility(View.VISIBLE);
                                    img3.setVisibility(View.VISIBLE);
                                    overlayTextImg.setVisibility(View.GONE);

                                    // ================= For selectionCount=4: All images 125dp x 125.5dp =================
                                    float heightInDp = 125.5f;
                                    float widthInDp4 = 125f;

                                    int heightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            heightInDp,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    int widthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            widthInDp4,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    float cornerRadius = TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            20,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    // ================= For img1 (Top-left corner rounded) =================
                                    ViewGroup.LayoutParams params1 = img1.getLayoutParams();
                                    if (params1 != null) {
                                        params1.height = heightInPx;
                                        params1.width = widthInPx;
                                        img1.setLayoutParams(params1);
                                    }

                                    ShapeAppearanceModel shapeModel1 = img1.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img1.setShapeAppearanceModel(shapeModel1);

                                    // ================= For img2 (Bottom-left corner rounded) =================
                                    Log.d("SelectionBunch", "=== IMG2 SETUP START for selectionCount=4 ===");
                                    Log.d("SelectionBunch", "img2 initial state - visibility: " + img2.getVisibility() +
                                            ", width: " + img2.getWidth() +
                                            ", height: " + img2.getHeight());

                                    ViewGroup.LayoutParams params2 = img2.getLayoutParams();
                                    if (params2 != null) {
                                        Log.d("SelectionBunch", "img2 original layoutParams: " + params2.width + "x" + params2.height);
                                        params2.height = heightInPx;
                                        params2.width = widthInPx;
                                        img2.setLayoutParams(params2);

                                        // Force layout pass to apply the new dimensions
                                        img2.requestLayout();
                                        img2.invalidate();

                                        Log.d("SelectionBunch", "Set img2 dimensions for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125.5dp)");
                                        Log.d("SelectionBunch", "img2 after setLayoutParams - width: " + img2.getWidth() +
                                                ", height: " + img2.getHeight());
                                    } else {
                                        Log.e("SelectionBunch", "img2 layoutParams is NULL!");
                                    }

                                    ShapeAppearanceModel shapeModel2 = img2.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img2.setShapeAppearanceModel(shapeModel2);

                                    // ================= For img3 (Top-right corner rounded) =================
                                    ViewGroup.LayoutParams params3 = img3.getLayoutParams();
                                    if (params3 != null) {
                                        params3.height = heightInPx;
                                        params3.width = widthInPx;
                                        img3.setLayoutParams(params3);
                                    }

                                    ShapeAppearanceModel shapeModel3 = img3.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img3.setShapeAppearanceModel(shapeModel3);

                                    // ================= For img4 (Bottom-right corner rounded) =================
                                    ViewGroup.LayoutParams params4 = img4.getLayoutParams();
                                    if (params4 != null) {
                                        params4.height = heightInPx;
                                        params4.width = widthInPx;
                                        img4.setLayoutParams(params4);
                                    }

                                    ShapeAppearanceModel shapeModel4 = img4.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .build();

                                    img4.setShapeAppearanceModel(shapeModel4);

                                    Log.d("SelectionBunch", "Set all images for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");

                                    Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                            + ", selectionCount=" + model.getSelectionCount()
                                            + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                                    bindSelectionBunchImagesSenderLong( model, requestOptions, position, true,img1,img2,img3,img4,img4Lyt,videoicon,overlayTextImg);

                                } else {


                                    senderImgLyt.setVisibility(View.GONE);
                                    senderImgBunchLyt.setVisibility(View.VISIBLE);
                                    senderImg.setVisibility(View.GONE);
                                    img2.setVisibility(View.VISIBLE);
                                    img4.setVisibility(View.VISIBLE);
                                    img4Lyt.setVisibility(View.VISIBLE);
                                    img1.setVisibility(View.VISIBLE);
                                    img3.setVisibility(View.VISIBLE);
                                    overlayTextImg.setVisibility(View.VISIBLE);


                                    int a = Integer.parseInt(model.getSelectionCount());

                                    overlayTextImg.setText("+ "+String.valueOf(a-3));

                                    // ================= For selectionCount=4: All images 125dp x 125.5dp =================
                                    float heightInDp = 125.5f;
                                    float widthInDp4 = 125f;

                                    int heightInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            heightInDp,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    int widthInPx = (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            widthInDp4,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    float cornerRadius = TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            20,
                                            img1.getResources().getDisplayMetrics()
                                    );

                                    // ================= For img1 (Top-left corner rounded) =================
                                    ViewGroup.LayoutParams params1 = img1.getLayoutParams();
                                    if (params1 != null) {
                                        params1.height = heightInPx;
                                        params1.width = widthInPx;
                                        img1.setLayoutParams(params1);
                                    }

                                    ShapeAppearanceModel shapeModel1 = img1.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img1.setShapeAppearanceModel(shapeModel1);

                                    // ================= For img2 (Bottom-left corner rounded) =================
                                    Log.d("SelectionBunch", "=== IMG2 SETUP START for selectionCount=4 ===");
                                    Log.d("SelectionBunch", "img2 initial state - visibility: " + img2.getVisibility() +
                                            ", width: " + img2.getWidth() +
                                            ", height: " + img2.getHeight());

                                    ViewGroup.LayoutParams params2 = img2.getLayoutParams();
                                    if (params2 != null) {
                                        Log.d("SelectionBunch", "img2 original layoutParams: " + params2.width + "x" + params2.height);
                                        params2.height = heightInPx;
                                        params2.width = widthInPx;
                                        img2.setLayoutParams(params2);

                                        // Force layout pass to apply the new dimensions
                                        img2.requestLayout();
                                        img2.invalidate();

                                        Log.d("SelectionBunch", "Set img2 dimensions for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125.5dp)");
                                        Log.d("SelectionBunch", "img2 after setLayoutParams - width: " + img2.getWidth() +
                                                ", height: " + img2.getHeight());
                                    } else {
                                        Log.e("SelectionBunch", "img2 layoutParams is NULL!");
                                    }

                                    ShapeAppearanceModel shapeModel2 = img2.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img2.setShapeAppearanceModel(shapeModel2);

                                    // ================= For img3 (Top-right corner rounded) =================
                                    ViewGroup.LayoutParams params3 = img3.getLayoutParams();
                                    if (params3 != null) {
                                        params3.height = heightInPx;
                                        params3.width = widthInPx;
                                        img3.setLayoutParams(params3);
                                    }

                                    ShapeAppearanceModel shapeModel3 = img3.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
                                            .build();

                                    img3.setShapeAppearanceModel(shapeModel3);

                                    // ================= For img4 (Bottom-right corner rounded) =================
                                    ViewGroup.LayoutParams params4 = img4.getLayoutParams();
                                    if (params4 != null) {
                                        params4.height = heightInPx;
                                        params4.width = widthInPx;
                                        img4.setLayoutParams(params4);
                                    }

                                    ShapeAppearanceModel shapeModel4 = img4.getShapeAppearanceModel()
                                            .toBuilder()
                                            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                                            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                                            .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                                            .build();

                                    img4.setShapeAppearanceModel(shapeModel4);

                                    Log.d("SelectionBunch", "Set all images for selectionCount=4: " + widthInPx + "x" + heightInPx + "px (125dp x 125dp)");

                                    Log.d("SelectionBunch", "Binding selectionBunch for messageId=" + model.getModelId()
                                            + ", selectionCount=" + model.getSelectionCount()
                                            + ", bunchSize=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

                                    bindSelectionBunchImagesSenderLong( model, requestOptions, position, true,img1,img2,img3,img4,img4Lyt,videoicon,overlayTextImg);
                                }
                            } else {
                                senderImgLyt.setVisibility(View.VISIBLE);

                                senderImg.setVisibility(View.VISIBLE);
                                senderImgBunchLyt.setVisibility(View.GONE);
                            }


                            docLyt.setVisibility(View.GONE);
                            senderVideo.setVisibility(View.GONE);
                            sendervideoLyt.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.GONE);
                            miceContainer.setVisibility(View.GONE);
                            if (!model.getCaption().equals("")) {
                                captionText.setVisibility(View.VISIBLE);
                                captionText.setText(model.getCaption());
                            } else {
                                captionText.setVisibility(View.GONE);
                            }

// Open document on tapping any document UI element (more reliable than itemView)
                            View.OnClickListener openDocClick = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    File customFolder;
                                    String exactPath = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                        exactPath = customFolder.getAbsolutePath();
                                    } else {
                                        customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                                        exactPath = customFolder.getAbsolutePath();
                                    }

                                    if (doesFileExist(exactPath + "/" + model.getFileName())) {
                                        Intent intent = new Intent(mContext, show_document_screen.class);
                                        intent.putExtra("documentKey", model.getFileName());
                                        intent.putExtra("nameKey", model.getFileName());
                                        intent.putExtra("sizeKey", model.getDocSize());
                                        intent.putExtra("extensionKey", model.getExtension());
                                        intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                    } else {
                                        Intent intent = new Intent(mContext, show_document_screen.class);
                                        intent.putExtra("documentKey", model.getDocument());
                                        intent.putExtra("nameKey", model.getFileName());
                                        intent.putExtra("sizeKey", model.getDocSize());
                                        intent.putExtra("extensionKey", model.getExtension());
                                        intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                    }
                                }
                            };


                            File customFolder2;
                            String exactPath2;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder2 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                                exactPath2 = customFolder2.getAbsolutePath();
                            } else {
                                customFolder2 = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                                exactPath2 = customFolder2.getAbsolutePath();
                            }

                            if (doesFileExist(exactPath2 + "/" + model.getFileName())) {
                                Log.d("TAG", "status: " + "existing");

                                try {
                                    String imageSource = exactPath2 + "/" + model.getFileName();
                                    ImageView targetImageView = senderImg;
                                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();

                                    Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);
                                    //  setImageViewDimensions(targetImageView, model.getImageWidth(), model.getImageHeight());
                                } catch (Exception e) {
                                    senderImg.setVisibility(View.GONE);
                                    Log.d("ImageLoad", "Exception loading sender image: " + e.getMessage());
                                }

                                downlaod.setVisibility(View.GONE);
                                progressBarImageview.setVisibility(View.GONE);
                            } else {
                                try {
                                    String imageSource = model.getDocument();
                                    ImageView targetImageView = senderImg;
                                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                    Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);
                                    //   setImageViewDimensions(targetImageView, model.getImageWidth(), model.getImageHeight());
                                } catch (Exception e) {
                                    senderImg.setVisibility(View.GONE);
                                    Log.d("ImageLoad", "Exception loading senderDoc image: " + e.getMessage());
                                }

                                downlaod.setVisibility(View.VISIBLE);
                                progressBarImageview.setVisibility(View.GONE);
                            }


                            LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                            deleteLyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                    // Print all group members before deleting
                                    printAllGroupMembers();

                                    database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                    Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                        @Override
                                                        public void onMembersReceived(ArrayList<members> groupMembers) {
                                                            if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                // Use background thread for smooth deletion
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (int i = 0; i < groupMembers.size(); i++) {
                                                                            members member = groupMembers.get(i);
                                                                            final int memberIndex = i;

                                                                            // Extract member UID
                                                                            String memberUid = extractMemberUid(member);

                                                                            if (memberUid != null && !memberUid.isEmpty()) {
                                                                                // Delete from CHAT database for each member using memberUid
                                                                                database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                            }
                                                                                        });

                                                                                // Call delete API for individual chatting
                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                // Add delay between operations for smooth execution
                                                                                try {
                                                                                    Thread.sleep(100);
                                                                                } catch (InterruptedException e) {
                                                                                    Thread.currentThread().interrupt();
                                                                                }
                                                                            }
                                                                        }

                                                                        // After all members processed, complete the deletion
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // Delete from local SQLite database
                                                                                try {
                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());


                                                                                } catch (Exception e) {

                                                                                    throw new RuntimeException(e);
                                                                                }

                                                                                // Remove item from UI
                                                                                removeItem(holder.getAdapterPosition());

                                                                                // Dismiss the delete dialog
                                                                                dismissDeleteDialog();

                                                                                Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();

                                                            } else {
                                                                Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                // Fallback to basic deletion if no members
                                                                database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                try {
                                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                } catch (Exception e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                removeItem(holder.getAdapterPosition());

                                                                                                // Dismiss the delete dialog
                                                                                                dismissDeleteDialog();

                                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                            return true;
                        }
                    });

                } else
                if (model.getDataType().equals(Constant.video)) {
                    Log.d("TAG444", "com");
                    ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                    ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderVideo.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                    ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                    if (!model.getCaption().equals("")) {
                        ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).captionText.setText(model.getCaption());

                    } else {
                        ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                    }


                    File customFolder3;
                    File customFolderVideo;
                    String exactPath2, exactPath4;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder3 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                        customFolderVideo = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                        exactPath2 = customFolder3.getAbsolutePath();
                        exactPath4 = customFolderVideo.getAbsolutePath();
                    } else {

                        customFolder3 = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Thumbnail");
                        customFolderVideo = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                        exactPath2 = customFolder3.getAbsolutePath();
                        exactPath4 = customFolderVideo.getAbsolutePath();

                    }

                    if (doesFileExist(exactPath2 + "/" + model.getFileNameThumbnail())) {
                        ((senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
                        ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);

                        Log.d("TAG", "getFileNameThumbnail: " + model.getFileNameThumbnail());

                        try {

                            String imageSource = exactPath2 + "/" + model.getFileNameThumbnail();
                            ImageView targetImageView = ((senderViewHolder) holder).senderVideo;
                            ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                            Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, ((senderViewHolder) holder).videoicon);


                        } catch (Exception e) {
                            ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                            Log.d("VideoThumb", "Exception: " + e.getMessage());
                        }


                        if (doesFileExist(exactPath4 + "/" + model.getFileName())) {
                            ((senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
                            ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                        } else {
                            ((senderViewHolder) holder).downlaodVideo.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                        }

                    } else {

                        ((senderViewHolder) holder).downlaodVideo.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);

                        try {
                            String imageSource = model.getThumbnail();
                            ImageView targetImageView = ((senderViewHolder) holder).senderVideo;
                            ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                            Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, ((senderViewHolder) holder).videoicon);


                        } catch (Exception e) {
                            ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                            Log.d("ThumbLoad", "Exception loading thumbnail: " + e.getMessage());
                        }


                        // Wire up click to play via foreground service and bottom sheet (sender side)
                        ((senderViewHolder) holder).micePlay.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).micePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String audioUrl = model.getDocument();
                                String profileImageUrl = model.getMicPhoto() != null ? model.getMicPhoto() : "";
                                String songTitle = model.getFileName() != null ? model.getFileName() : "Audio Message";
                                String localFilePath = null;

                                // Prefer cacheDir (matches our download location)
                                File cacheDir = mContext.getCacheDir();
                                if (cacheDir != null) {
                                    File cached = new File(cacheDir, model.getFileName());
                                    localFilePath = cached.getAbsolutePath();
                                    if (doesFileExist(localFilePath)) {
                                        audioUrl = localFilePath;
                                    } else {
                                        // Fallback: external files music dir
                                        File musicDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                        if (musicDir != null) {
                                            String altPath = new File(musicDir, model.getFileName()).getAbsolutePath();
                                            if (doesFileExist(altPath)) {
                                                localFilePath = altPath;
                                                audioUrl = altPath;
                                            }
                                        }
                                    }
                                }

                                Intent serviceIntent = new Intent(mContext, AudioPlaybackServiceGroup.class);
                                serviceIntent.putExtra("audioUrl", audioUrl);
                                serviceIntent.putExtra("profileImageUrl", profileImageUrl);
                                serviceIntent.putExtra("songTitle", songTitle);
                                serviceIntent.putExtra("localFilePath", localFilePath);
                                serviceIntent.putExtra("modelId", model.getModelId());
                                serviceIntent.putExtra("position", holder.getAdapterPosition());
                                // Provide group/chat navigation context for notification tap
                                serviceIntent.putExtra("grpIdKey", grpIdKey);
                                serviceIntent.putExtra("captionKey", caption);
                                serviceIntent.putExtra("nameKey", name);
                                ContextCompat.startForegroundService(mContext, serviceIntent);

                                // Show bottom sheet UI
                                if (mContext instanceof AppCompatActivity) {
                                    MusicPlayerBottomSheetGroup bottomSheet = MusicPlayerBottomSheetGroup.newInstance(audioUrl, profileImageUrl, songTitle, grpIdKey, name, caption);
                                    bottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), MusicPlayerBottomSheetGroup.TAG);
                                } else {
                                    Log.e("MusicPlayer", "mContext is not an AppCompatActivity: " + mContext.getClass().getSimpleName());
                                }
                            }
                        });

                    }


                    ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                    ((senderViewHolder) holder).miceContainer.setVisibility(View.GONE);

                    ((senderViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @OptIn(markerClass = androidx.media3.common.util.UnstableApi.class)

                        @Override
                        public void onClick(View v) {


                            File customFolder;
                            String exactPath = null;
                            //android 10
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                                exactPath = customFolder.getAbsolutePath();
                                Log.d("TAG", "exactPath: " + exactPath + "/" + model.getFileName());
                            } else {
                                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                                exactPath = customFolder.getAbsolutePath();
                            }

                            if (doesFileExist(exactPath + "/" + model.getFileName())) {
                                Intent intent = new Intent(mContext, show_video_playerScreen.class);
                                intent.putExtra("videoUri", model.getFileName());
                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            } else {
//                                Intent intent = new Intent(mContext, show_video_playerScreen.class);
//                                intent.putExtra("videoUri", model.getDocument());
//                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
//                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            }


                        }
                    });
                    ((senderViewHolder) holder).sendervideoLyt.setOnClickListener(new View.OnClickListener() {
                        @OptIn(markerClass = UnstableApi.class)
                        @Override
                        public void onClick(View v) {


                            File customFolder;
                            String exactPath = null;
                            //android 10
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                                exactPath = customFolder.getAbsolutePath();
                                Log.d("TAG", "exactPath: " + exactPath + "/" + model.getFileName());
                            } else {
                                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                                exactPath = customFolder.getAbsolutePath();
                            }

                            if (doesFileExist(exactPath + "/" + model.getFileName())) {
                                //      Toast.makeText(mContext, "doesFileExist", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(mContext, show_video_playerScreen.class);
                                intent.putExtra("videoUri", model.getFileName());
                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            } else {
//                                Intent intent = new Intent(mContext, show_video_playerScreen.class);
//                                intent.putExtra("videoUri", model.getDocument());
//                                intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
//                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            }


                        }
                    });
                    ((senderViewHolder) holder).downlaodVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startSenderVideoDownloadWithProgress(holder, model);

                        }
                    });

                    ((senderViewHolder) holder).sendervideoLyt.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            float touchX = location[0];
                            float touchY = location[1];


                            BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                            BlurHelper.dialogLayoutColor.show();
                            RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                            // Use boundary-aware positioning
                            BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                            TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                            TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                            TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                            TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                            TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                            TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                            TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                            TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                            TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                            TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                            RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                            RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                            RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                            ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                            ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                            ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                            ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                            ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                            LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                            AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                            LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                            LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                            LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                            copy.setVisibility(View.GONE);
                            LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                            LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                            LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                            View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                            View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                            View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                            View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                            CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                            TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                            FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                            LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                            LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                            CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                            LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                            TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                            LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                            CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                            CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                            ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                            TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                            ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                            TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                            RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                            FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                            ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                            TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                            ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                            RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                            ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                            ProgressBar progressBarVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarVideo);
                            TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                            FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                            FloatingActionButton downlaodVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodVideo);
                            ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                            CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                            LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                            TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                            TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                            TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);


                            AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                            LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                            LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                            int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                // Dark mode is active
                                Constant.getSfFuncion(mContext);
                                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                ColorStateList tintList;

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#00A3E9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#7adf2a")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#ec0001")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#16f3ff")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#FF8A00")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#7F7F7F")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#D9B845")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#346667")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#9846D9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#A81010")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }
                                } catch (Exception ignored) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    MainSenderBox.setBackgroundTintList(tintList);
                                    richBox.setBackgroundTintList(tintList);


                                }


                            } else {
                                tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                            }

                            // todo theme
                            try {

                                Constant.getSfFuncion(mContext);
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#00A3E9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7adf2a")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#ec0001")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#16f3ff")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#FF8A00")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7F7F7F")) {


                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    } else if (themColor.equals("#D9B845")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#346667")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#9846D9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#A81010")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    }
                                } catch (Exception ignored) {

                                    viewnew.setBackgroundTintList(tintList);
                                    readMore.setTextColor(Color.parseColor(themColor));

                                }


                            } catch (Exception ignored) {
                            }
                            Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                            cardview.startAnimation(animationdd);
                            sendTime.setText(model.getTime());


                            // main code start from here
                            Log.d("TAG444", "com");
                            Log.d("Mdid", model.getModelId());
                            readMore.setVisibility(View.GONE);
                            richLinkViewLyt.setVisibility(View.GONE);
                            sendMessage.setVisibility(View.GONE);
                            senderVideo.setVisibility(View.VISIBLE);
                            sendervideoLyt.setVisibility(View.VISIBLE);
                            senderImg.setVisibility(View.GONE);
                            docLyt.setVisibility(View.GONE);
                            if (!model.getCaption().equals("")) {
                                captionText.setVisibility(View.VISIBLE);
                                captionText.setText(model.getCaption());
                            } else {
                                captionText.setVisibility(View.GONE);
                            }

                            File customFolder3;
                            File customFolderVideo;
                            String exactPath2, exactPath4;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                customFolder3 = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
                                customFolderVideo = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
                                exactPath2 = customFolder3.getAbsolutePath();
                                exactPath4 = customFolderVideo.getAbsolutePath();
                            } else {
                                customFolder3 = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Thumbnail");
                                customFolderVideo = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Videos");
                                exactPath2 = customFolder3.getAbsolutePath();
                                exactPath4 = customFolderVideo.getAbsolutePath();
                            }

                            if (doesFileExist(exactPath2 + "/" + model.getFileNameThumbnail())) {
                                downlaodVideo.setVisibility(View.GONE);
                                progressBarVideo.setVisibility(View.GONE);

                                Log.d("TAG", "getFileNameThumbnail: " + model.getFileNameThumbnail());

                                try {
                                    String imageSource = exactPath2 + "/" + model.getFileNameThumbnail();
                                    ImageView targetImageView = senderVideo;
                                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                    Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);
                                } catch (Exception e) {
                                    senderVideo.setVisibility(View.GONE);
                                    Log.d("VideoThumb", "Exception: " + e.getMessage());
                                }

                                if (doesFileExist(exactPath4 + "/" + model.getFileName())) {
                                    downlaodVideo.setVisibility(View.GONE);
                                    progressBarVideo.setVisibility(View.GONE);
                                } else {
                                    downlaodVideo.setVisibility(View.VISIBLE);
                                    progressBarVideo.setVisibility(View.GONE);
                                }
                            } else {
                                downlaodVideo.setVisibility(View.VISIBLE);
                                progressBarVideo.setVisibility(View.GONE);

                                try {
                                    String imageSource = model.getThumbnail();
                                    ImageView targetImageView = senderVideo;
                                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                    Constant.loadImageIntoViewGroupOptimized(mContext, imageSource, requestOptions, targetImageView, parentLayout, position, true, model, videoicon);
                                } catch (Exception e) {
                                    senderVideo.setVisibility(View.GONE);
                                    Log.d("ThumbLoad", "Exception loading thumbnail: " + e.getMessage());
                                }

                                // Wire up click to play via foreground service and bottom sheet (sender side)
                                micePlay.setVisibility(View.VISIBLE);

                            }

                            contactContainer.setVisibility(View.GONE);
                            miceContainer.setVisibility(View.GONE);


                            LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                            deleteLyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                    // Print all group members before deleting
                                    printAllGroupMembers();

                                    database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                    Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                        @Override
                                                        public void onMembersReceived(ArrayList<members> groupMembers) {
                                                            if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                // Use background thread for smooth deletion
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (int i = 0; i < groupMembers.size(); i++) {
                                                                            members member = groupMembers.get(i);
                                                                            final int memberIndex = i;

                                                                            // Extract member UID
                                                                            String memberUid = extractMemberUid(member);

                                                                            if (memberUid != null && !memberUid.isEmpty()) {
                                                                                // Delete from CHAT database for each member using memberUid
                                                                                database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                            }
                                                                                        });

                                                                                // Call delete API for individual chatting
                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                // Add delay between operations for smooth execution
                                                                                try {
                                                                                    Thread.sleep(100);
                                                                                } catch (InterruptedException e) {
                                                                                    Thread.currentThread().interrupt();
                                                                                }
                                                                            }
                                                                        }

                                                                        // After all members processed, complete the deletion
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // Delete from local SQLite database
                                                                                try {
                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                } catch (Exception e) {
                                                                                    throw new RuntimeException(e);
                                                                                }

                                                                                // Remove item from UI
                                                                                removeItem(holder.getAdapterPosition());

                                                                                // Dismiss the delete dialog
                                                                                dismissDeleteDialog();

                                                                                Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();

                                                            } else {
                                                                Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                // Fallback to basic deletion if no members
                                                                database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                try {
                                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                } catch (Exception e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                removeItem(holder.getAdapterPosition());

                                                                                                // Dismiss the delete dialog
                                                                                                dismissDeleteDialog();

                                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                            return true;
                        }
                    });



                } else
                if (model.getDataType().equals(Constant.Text)) {
                    Log.d("TAG444", "com");


                    String content = model.getMessage();

                    switch (Constant.analyzeTextContent(content)) {
                        case "only_emoji":
                            System.out.println("The text contains only emojis.");
                            // Handle emoji-only logic here

                            int emojiCount = Constant.countEmojis(model.getMessage());
                            System.out.println("Emoji detected! " + model.getMessage() + " " + emojiCount);

                            if (emojiCount == 1) {
                                //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),30);
                                ((senderViewHolder) holder).sendMessage.setText(model.getMessage()); // Set formatted text
                                ((senderViewHolder) holder).MainSenderBox.setBackground(null); // Set formatted text

                                if (((senderViewHolder) holder).viewnew.getVisibility() == View.VISIBLE) {

                                    ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE); // Set formatted text

                                }


                                ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);

                                // Add tap listener to animate emoji on tap
                                Constant.setupEmojiTapAnimation(((senderViewHolder) holder).sendMessage, model.getMessage());
                            } else if (emojiCount == 2) {
//                                    SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),25);
                                ((senderViewHolder) holder).sendMessage.setText(model.getMessage()); // Set formatted text
                                ((senderViewHolder) holder).MainSenderBox.setBackground(null); // Set formatted text
                                // Show progress indicator (animate if last item is sending)
                                // Always show viewnew
                                ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                                if (position == groupMessageList.size() - 1) {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                                } else {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                                }
                                ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                            } else if (emojiCount == 3) {
//                                    SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),20);
                                ((senderViewHolder) holder).sendMessage.setText(model.getMessage()); // Set formatted text
                                ((senderViewHolder) holder).MainSenderBox.setBackground(null); // Set formatted text
                                // Show progress indicator (animate if last item is sending)
                                // Always show viewnew
                                ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                                if (position == groupMessageList.size() - 1) {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                                } else {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                                }
                                ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                            } else if (emojiCount == 4) {
//                                    SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),18);
                                ((senderViewHolder) holder).sendMessage.setText(model.getMessage()); // Set formatted text
                                ((senderViewHolder) holder).MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                // Show progress indicator (animate if last item is sending)
                                // Always show viewnew
                                ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                                if (position == groupMessageList.size() - 1) {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                                } else {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                                }
                                ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            } else {
//                                    SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),18);
                                ((senderViewHolder) holder).sendMessage.setText(model.getMessage()); // Set formatted text
                                ((senderViewHolder) holder).MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                // Show progress indicator (animate if last item is sending)
                                // Always show viewnew
                                ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                                if (position == groupMessageList.size() - 1) {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                                } else {
                                    ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                                }
                                ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            }


                            break;

                        case "text_and_emoji":
                            System.out.println("The text contains both text and emojis.");
                            // Handle mixed text + emoji logic here
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                            // Show progress indicator (animate if last item is sending)
                            // Always show viewnew
                            ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                            if (position == groupMessageList.size() - 1) {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                            } else {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                            }
                            ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                            break;

                        case "only_text":
                            System.out.println("The text contains only text.");
                            // Handle text-only logic here
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                            // Show progress indicator (animate if last item is sending)
                            // Always show viewnew
                            ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                            if (position == groupMessageList.size() - 1) {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                            } else {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                            }
                            ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                            break;

                        default:
                            System.out.println("Unexpected content.");
                            ((senderViewHolder) holder).MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                            // Show progress indicator (animate if last item is sending)
                            // Always show viewnew
                            ((senderViewHolder) holder).viewnew.setVisibility(View.VISIBLE);
                            if (position == groupMessageList.size() - 1) {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(isLastItemVisible);
                            } else {
                                ((senderViewHolder) holder).viewnew.setIndeterminate(false);
                            }
                            ((senderViewHolder) holder).sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                            break;
                    }


                    if (URLUtil.isValidUrl(model.getMessage())) {


                        ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                        ((senderViewHolder) holder).linkActualUrl.setVisibility(View.GONE);
                        ((senderViewHolder) holder).link.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).linkImg2.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).linkDesc.setVisibility(View.VISIBLE);


                        // todo for color of a theme purpose
                        try {

                            Constant.getSfFuncion(mContext);
                            themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                            tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                            try {
                                if (themColor.equals("#ff0080")) {

                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#00A3E9")) {

                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));
                                } else if (themColor.equals("#7adf2a")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#ec0001")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#16f3ff")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#FF8A00")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#7F7F7F")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#D9B845")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));
                                } else if (themColor.equals("#346667")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#9846D9")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else if (themColor.equals("#A81010")) {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));

                                } else {


                                    ((senderViewHolder) holder).link.setTextColor(Color.parseColor(themColor));
                                    ((senderViewHolder) holder).linkActualUrl.setTextColor(Color.parseColor(themColor));
                                }
                            } catch (Exception ignored) {

                            }


                        } catch (Exception ignored) {
                        }
                        try {
//
                            // todo after sqlite
                            try {


                                linkPreviewModel linkPreviewModel = new DatabaseHelper(mContext).getAllLinkPreviewModel(model.getModelId());


                                if (linkPreviewModel.getUrl().equals("")) {
                                    ((senderViewHolder) holder).linkActualUrl.setVisibility(View.VISIBLE);
                                    ((senderViewHolder) holder).linkActualUrl.setText(model.getMessage());
                                    ((senderViewHolder) holder).linkActualUrl.setPaintFlags(((senderViewHolder) holder).link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    ((senderViewHolder) holder).link.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).linkTitle.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).linkImg.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).linkImg2.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).linkDesc.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).link.setVisibility(View.GONE);


                                } else {
                                    ((senderViewHolder) holder).linkActualUrl.setVisibility(View.GONE);
                                    ((senderViewHolder) holder).link.setVisibility(View.VISIBLE);
                                    ((senderViewHolder) holder).linkTitle.setVisibility(View.VISIBLE);
                                    ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                    ((senderViewHolder) holder).linkImg2.setVisibility(View.VISIBLE);


                                    if (linkPreviewModel.getDescription().equals("")) {
                                        ((senderViewHolder) holder).linkDesc.setVisibility(View.GONE);
                                    } else {
                                        ((senderViewHolder) holder).linkDesc.setVisibility(View.VISIBLE);
                                        ((senderViewHolder) holder).linkDesc.setText(linkPreviewModel.getDescription());
                                    }


                                    if (linkPreviewModel.getImage_url().equals("")) {
                                        ((senderViewHolder) holder).linkImg.setVisibility(View.GONE);
                                    } else {
                                        ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                        try {
                                            // Using Constant method instead of direct Picasso
                                            Constant.loadSimpleImage(((senderViewHolder) holder).linkImg.getContext(),
                                                    linkPreviewModel.getImage_url(),
                                                    ((senderViewHolder) holder).linkImg,
                                                    R.drawable.link_svg,
                                                    R.drawable.link_svg);
                                        } catch (Exception e) {
                                        }
                                    }


                                    if (linkPreviewModel.getFavIcon().equals("")) {
                                        ((senderViewHolder) holder).linkImg2.setImageResource(R.drawable.link_fav);
                                    } else {
                                        ((senderViewHolder) holder).linkImg2.setVisibility(View.VISIBLE);

                                        try {
                                            Constant.loadSimpleImage(((senderViewHolder) holder).linkImg2.getContext(),
                                                    linkPreviewModel.getFavIcon(),
                                                    ((senderViewHolder) holder).linkImg2,
                                                    R.drawable.link_fav,
                                                    R.drawable.link_fav);
                                        } catch (Exception e) {
                                        }

                                    }


                                    if (linkPreviewModel.getFavIcon().equals("") && linkPreviewModel.getDescription().equals("") && linkPreviewModel.getImage_url().equals("")) {
                                        ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                    }


                                    ((senderViewHolder) holder).linkTitle.setText(linkPreviewModel.getTitle());

                                    ((senderViewHolder) holder).link.setText(linkPreviewModel.getUrl());
                                    ((senderViewHolder) holder).link.setPaintFlags(((senderViewHolder) holder).link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                }

                                Log.d("TAG", "onBindViewHolder: " + linkPreviewModel.getTitle());


                            } catch (Exception e) {
                                // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            RichPreview richPreview = new RichPreview(new ResponseListener() {
                                @Override
                                public void onData(MetaData metaData) {
                                    // todo here need to store data to sqllite


                                    if (isInternetConnected()) {
                                        try {
                                            new DatabaseHelper(mContext).insert_linkPreviewTable(mContext, model.getModelId(), metaData.getUrl(), metaData.getTitle(), metaData.getDescription(), metaData.getFavicon(), metaData.getImageurl());


                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                        if (position == groupMessageList.size() - 1) {
                                            // todo after sqlite
                                            try {


                                                linkPreviewModel linkPreviewModel = new DatabaseHelper(mContext).getAllLinkPreviewModel(model.getModelId());


                                                if (linkPreviewModel.getUrl().equals("")) {
                                                    ((senderViewHolder) holder).linkActualUrl.setVisibility(View.VISIBLE);
                                                    ((senderViewHolder) holder).linkActualUrl.setText(model.getMessage());
                                                    ((senderViewHolder) holder).linkActualUrl.setPaintFlags(((senderViewHolder) holder).link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                                    ((senderViewHolder) holder).link.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).linkTitle.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).linkImg.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).linkImg2.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).linkDesc.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).link.setVisibility(View.GONE);


                                                } else {
                                                    ((senderViewHolder) holder).linkActualUrl.setVisibility(View.GONE);
                                                    ((senderViewHolder) holder).link.setVisibility(View.VISIBLE);
                                                    ((senderViewHolder) holder).linkTitle.setVisibility(View.VISIBLE);
                                                    ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                                    ((senderViewHolder) holder).linkImg2.setVisibility(View.VISIBLE);


                                                    if (linkPreviewModel.getDescription().equals("")) {
                                                        ((senderViewHolder) holder).linkDesc.setVisibility(View.GONE);
                                                    } else {
                                                        ((senderViewHolder) holder).linkDesc.setVisibility(View.VISIBLE);
                                                        ((senderViewHolder) holder).linkDesc.setText(linkPreviewModel.getDescription());
                                                    }


                                                    if (linkPreviewModel.getImage_url().equals("")) {
                                                        ((senderViewHolder) holder).linkImg.setVisibility(View.GONE);
                                                    } else {
                                                        ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                                        try {
                                                            // Using Constant method instead of direct Picasso
                                                            Constant.loadSimpleImage(((senderViewHolder) holder).linkImg.getContext(),
                                                                    linkPreviewModel.getImage_url(),
                                                                    ((senderViewHolder) holder).linkImg,
                                                                    R.drawable.link_svg,
                                                                    R.drawable.link_svg);
                                                        } catch (Exception e) {
                                                        }
                                                    }


                                                    if (linkPreviewModel.getFavIcon().equals("")) {
                                                        ((senderViewHolder) holder).linkImg2.setImageResource(R.drawable.link_fav);
                                                    } else {
                                                        ((senderViewHolder) holder).linkImg2.setVisibility(View.VISIBLE);

                                                        try {
                                                            Constant.loadSimpleImage(((senderViewHolder) holder).linkImg2.getContext(),
                                                                    linkPreviewModel.getFavIcon(),
                                                                    ((senderViewHolder) holder).linkImg2,
                                                                    R.drawable.link_fav,
                                                                    R.drawable.link_fav);
                                                        } catch (Exception e) {
                                                        }

                                                    }

                                                    if (linkPreviewModel.getFavIcon().equals("") && linkPreviewModel.getDescription().equals("") && linkPreviewModel.getImage_url().equals("")) {
                                                        ((senderViewHolder) holder).linkImg.setVisibility(View.VISIBLE);
                                                    }

                                                    ((senderViewHolder) holder).linkTitle.setText(linkPreviewModel.getTitle());

                                                    ((senderViewHolder) holder).link.setText(linkPreviewModel.getUrl());
                                                    ((senderViewHolder) holder).link.setPaintFlags(((senderViewHolder) holder).link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                                }


                                            } catch (Exception e) {

                                            }
                                        }
                                    }


                                }

                                @Override
                                public void onError(Exception e) {

                                    Log.d("RICHLINK", "onError: " + e.getMessage());
//                                    try {
//                                        ((senderViewHolder)holder).linkActualUrl.setVisibility(View.VISIBLE);
//                                        ((senderViewHolder)holder).linkActualUrl.setText(model.getMessage());
//                                        ((senderViewHolder)holder).link.setVisibility(View.GONE);
//                                        ((senderViewHolder)holder).linkImg.setVisibility(View.GONE);
//                                        ((senderViewHolder)holder).linkImg2.setVisibility(View.GONE);
//                                        ((senderViewHolder)holder).linkDesc.setVisibility(View.GONE);
//                                    } catch (Exception ex) {
//                                        throw new RuntimeException(ex);
//                                    }
                                }

                            });


                            richPreview.getPreview(model.getMessage());


                        } catch (Exception ignored) {
                            //   Toast.makeText(mContext, ignored.getMessage(), Toast.LENGTH_SHORT).show();

                            Log.d("TAG", "vsdvdsvs: " + ignored.getMessage());
                        }

                        ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                    } else {
                        ((senderViewHolder) holder).sendMessage.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                    }
                    ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                    ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                    ((senderViewHolder) holder).miceContainer.setVisibility(View.GONE);

                    // todo readmore and

                    if (model.getMessage().length() >= 200) {
                        ((senderViewHolder) holder).readMore.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).sendMessage.setText(model.getMessage().substring(0, 200));
                    } else {
                        ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                        ((senderViewHolder) holder).sendMessage.setText(model.getMessage());
                    }


                    if (!model.getCaption().equals("")) {
                        ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).captionText.setText(model.getCaption());

                    } else {
                        ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                    }


                    ((senderViewHolder) holder).readMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                            ((senderViewHolder) holder).sendMessage.setText(model.getMessage());
                        }
                    });

                    ((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            float touchX = location[0];
                            float touchY = location[1];


                            BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                            BlurHelper.dialogLayoutColor.show();
                            RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                            // Use boundary-aware positioning
                            BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                            TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                            TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                            TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                            TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                            TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                            TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                            TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                            TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                            TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                            TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                            RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                            RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                            RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                            ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                            ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                            ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                            ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                            ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                            LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                            AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                            LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                            LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                            LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                            copy.setVisibility(View.VISIBLE);
                            LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                            LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                            LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);
                            View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                            View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                            View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                            View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                            CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                            TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                            FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                            LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                            LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                            CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                            LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                            TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                            LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                            CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                            CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                            ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                            TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                            ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                            TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                            RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                            FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                            ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                            TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                            ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                            RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                            ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                            TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                            FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                            ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                            CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                            LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                            TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                            TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                            TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);

                            AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                            LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                            LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                            int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                // Dark mode is active
                                Constant.getSfFuncion(mContext);
                                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                ColorStateList tintList;

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#00A3E9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#7adf2a")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#ec0001")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#16f3ff")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#FF8A00")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#7F7F7F")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#D9B845")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#346667")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#9846D9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#A81010")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }
                                } catch (Exception ignored) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    MainSenderBox.setBackgroundTintList(tintList);
                                    richBox.setBackgroundTintList(tintList);


                                }


                            } else {
                                tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                            }

                            // todo theme
                            try {

                                Constant.getSfFuncion(mContext);
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#00A3E9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7adf2a")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#ec0001")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#16f3ff")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#FF8A00")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7F7F7F")) {


                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    } else if (themColor.equals("#D9B845")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#346667")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#9846D9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#A81010")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    }
                                } catch (Exception ignored) {

                                    viewnew.setBackgroundTintList(tintList);
                                    readMore.setTextColor(Color.parseColor(themColor));

                                }


                            } catch (Exception ignored) {
                            }
                            Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                            cardview.startAnimation(animationdd);
                            sendTime.setText(model.getTime());


                            // man coding is staring frm here
                            String content = model.getMessage();

                            switch (Constant.analyzeTextContent(content)) {
                                case "only_emoji":
                                    System.out.println("The text contains only emojis.");
                                    // Handle emoji-only logic here

                                    int emojiCount = Constant.countEmojis(model.getMessage());
                                    System.out.println("Emoji detected! " + model.getMessage() + " " + emojiCount);

                                    if (emojiCount == 1) {
                                        //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),30);
                                        sendMessage.setText(model.getMessage()); // Set formatted text
                                        MainSenderBox.setBackground(null); // Set formatted text

                                        if (viewnew.getVisibility() == View.VISIBLE) {
                                            viewnew.setVisibility(View.VISIBLE); // Set formatted text
                                        }

                                        sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);

                                        // Add tap listener to animate emoji on tap
                                        Constant.setupEmojiTapAnimation(sendMessage, model.getMessage());
                                    } else if (emojiCount == 2) {
                                        //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),25);
                                        sendMessage.setText(model.getMessage()); // Set formatted text
                                        MainSenderBox.setBackground(null); // Set formatted text
                                        // Show progress indicator (animate if last item is sending)
                                        // Always show viewnew
                                        viewnew.setVisibility(View.VISIBLE);

                                        sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
                                    } else if (emojiCount == 3) {
                                        //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),20);
                                        sendMessage.setText(model.getMessage()); // Set formatted text
                                        MainSenderBox.setBackground(null); // Set formatted text
                                        // Show progress indicator (animate if last item is sending)
                                        // Always show viewnew
                                        viewnew.setVisibility(View.VISIBLE);

                                        sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                                    } else if (emojiCount == 4) {
                                        //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),18);
                                        sendMessage.setText(model.getMessage()); // Set formatted text
                                        MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                        // Show progress indicator (animate if last item is sending)
                                        // Always show viewnew
                                        viewnew.setVisibility(View.VISIBLE);

                                        sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    } else {
                                        //  SpannableString formattedText = Constant.formatTextWithEmoji(model.getMessage(),18);
                                        sendMessage.setText(model.getMessage()); // Set formatted text
                                        MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                        // Show progress indicator (animate if last item is sending)
                                        // Always show viewnew
                                        viewnew.setVisibility(View.VISIBLE);

                                        sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    }
                                    break;

                                case "text_and_emoji":
                                    System.out.println("The text contains both text and emojis.");
                                    // Handle mixed text + emoji logic here
                                    MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                    // Show progress indicator (animate if last item is sending)
                                    // Always show viewnew
                                    viewnew.setVisibility(View.VISIBLE);

                                    sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    break;

                                case "only_text":
                                    System.out.println("The text contains only text.");
                                    // Handle text-only logic here
                                    MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                    // Show progress indicator (animate if last item is sending)
                                    // Always show viewnew
                                    viewnew.setVisibility(View.VISIBLE);

                                    sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    break;

                                default:
                                    System.out.println("Unexpected content.");
                                    MainSenderBox.setBackgroundResource(R.drawable.message_bg_blue); // Set formatted text
                                    // Show progress indicator (animate if last item is sending)
                                    // Always show viewnew
                                    viewnew.setVisibility(View.VISIBLE);

                                    sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    break;
                            }

                            if (URLUtil.isValidUrl(model.getMessage())) {
                                richLinkViewLyt.setVisibility(View.VISIBLE);
                                sendMessage.setVisibility(View.GONE);
                                linkActualUrl.setVisibility(View.GONE);
                                link.setVisibility(View.VISIBLE);
                                linkImg.setVisibility(View.VISIBLE);
                                linkImg2.setVisibility(View.VISIBLE);
                                linkDesc.setVisibility(View.VISIBLE);

                                // todo for color of a theme purpose
                                try {
                                    Constant.getSfFuncion(mContext);
                                    themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                    tintList = ColorStateList.valueOf(Color.parseColor(themColor));

                                    try {
                                        if (themColor.equals("#ff0080")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#00A3E9")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#7adf2a")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#ec0001")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#16f3ff")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#FF8A00")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#7F7F7F")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#D9B845")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#346667")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#9846D9")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else if (themColor.equals("#A81010")) {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        } else {
                                            link.setTextColor(Color.parseColor(themColor));
                                            linkActualUrl.setTextColor(Color.parseColor(themColor));
                                        }
                                    } catch (Exception ignored) {
                                    }
                                } catch (Exception ignored) {
                                }

                                try {
                                    // todo after sqlite
                                    try {
                                        linkPreviewModel linkPreviewModel = new DatabaseHelper(mContext).getAllLinkPreviewModel(model.getModelId());

                                        if (linkPreviewModel.getUrl().equals("")) {
                                            linkActualUrl.setVisibility(View.VISIBLE);
                                            linkActualUrl.setText(model.getMessage());
                                            linkActualUrl.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                            link.setVisibility(View.GONE);
                                            linkTitle.setVisibility(View.GONE);
                                            linkImg.setVisibility(View.GONE);
                                            linkImg2.setVisibility(View.GONE);
                                            linkDesc.setVisibility(View.GONE);
                                            link.setVisibility(View.GONE);
                                        } else {
                                            linkActualUrl.setVisibility(View.GONE);
                                            link.setVisibility(View.VISIBLE);
                                            linkTitle.setVisibility(View.VISIBLE);
                                            linkImg.setVisibility(View.VISIBLE);
                                            linkImg2.setVisibility(View.VISIBLE);

                                            if (linkPreviewModel.getDescription().equals("")) {
                                                linkDesc.setVisibility(View.GONE);
                                            } else {
                                                linkDesc.setVisibility(View.VISIBLE);
                                                linkDesc.setText(linkPreviewModel.getDescription());
                                            }

                                            if (linkPreviewModel.getImage_url().equals("")) {
                                                linkImg.setVisibility(View.GONE);
                                            } else {
                                                linkImg.setVisibility(View.VISIBLE);
                                                try {
                                                    // Using Constant method instead of direct Picasso
                                                    Constant.loadSimpleImage(linkImg.getContext(),
                                                            linkPreviewModel.getImage_url(),
                                                            linkImg,
                                                            R.drawable.link_svg,
                                                            R.drawable.link_svg);
                                                } catch (Exception e) {
                                                }
                                            }

                                            if (linkPreviewModel.getFavIcon().equals("")) {
                                                linkImg2.setImageResource(R.drawable.link_fav);
                                            } else {
                                                linkImg2.setVisibility(View.VISIBLE);
                                                try {
                                                    Constant.loadSimpleImage(linkImg2.getContext(),
                                                            linkPreviewModel.getFavIcon(),
                                                            linkImg2,
                                                            R.drawable.link_fav,
                                                            R.drawable.link_fav);
                                                } catch (Exception e) {
                                                }
                                            }

                                            if (linkPreviewModel.getFavIcon().equals("") && linkPreviewModel.getDescription().equals("") && linkPreviewModel.getImage_url().equals("")) {
                                                linkImg.setVisibility(View.VISIBLE);
                                            }

                                            linkTitle.setText(linkPreviewModel.getTitle());
                                            link.setText(linkPreviewModel.getUrl());
                                            link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                        }

                                        Log.d("TAG", "onBindViewHolder: " + linkPreviewModel.getTitle());
                                    } catch (Exception e) {
                                        // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    RichPreview richPreview = new RichPreview(new ResponseListener() {
                                        @Override
                                        public void onData(MetaData metaData) {
                                            // todo here need to store data to sqllite
                                            if (isInternetConnected()) {
                                                try {
                                                    new DatabaseHelper(mContext).insert_linkPreviewTable(mContext, model.getModelId(), metaData.getUrl(), metaData.getTitle(), metaData.getDescription(), metaData.getFavicon(), metaData.getImageurl());
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                if (position == groupMessageList.size() - 1) {
                                                    // todo after sqlite
                                                    try {
                                                        linkPreviewModel linkPreviewModel = new DatabaseHelper(mContext).getAllLinkPreviewModel(model.getModelId());

                                                        if (linkPreviewModel.getUrl().equals("")) {
                                                            linkActualUrl.setVisibility(View.VISIBLE);
                                                            linkActualUrl.setText(model.getMessage());
                                                            linkActualUrl.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                                            link.setVisibility(View.GONE);
                                                            linkTitle.setVisibility(View.GONE);
                                                            linkImg.setVisibility(View.GONE);
                                                            linkImg2.setVisibility(View.GONE);
                                                            linkDesc.setVisibility(View.GONE);
                                                            link.setVisibility(View.GONE);
                                                        } else {
                                                            linkActualUrl.setVisibility(View.GONE);
                                                            link.setVisibility(View.VISIBLE);
                                                            linkTitle.setVisibility(View.VISIBLE);
                                                            linkImg.setVisibility(View.VISIBLE);
                                                            linkImg2.setVisibility(View.VISIBLE);

                                                            if (linkPreviewModel.getDescription().equals("")) {
                                                                linkDesc.setVisibility(View.GONE);
                                                            } else {
                                                                linkDesc.setVisibility(View.VISIBLE);
                                                                linkDesc.setText(linkPreviewModel

                                                                        .getDescription());
                                                            }

                                                            if (linkPreviewModel.getImage_url().equals("")) {
                                                                linkImg.setVisibility(View.GONE);
                                                            } else {
                                                                linkImg.setVisibility(View.VISIBLE);
                                                                try {
                                                                    Constant.loadSimpleImage(linkImg.getContext(),
                                                                            linkPreviewModel.getImage_url(),
                                                                            linkImg,
                                                                            R.drawable.link_svg,
                                                                            R.drawable.link_svg);
                                                                } catch (Exception e) {
                                                                }
                                                            }

                                                            if (linkPreviewModel.getFavIcon().equals("")) {
                                                                linkImg2.setImageResource(R.drawable.link_fav);
                                                            } else {
                                                                linkImg2.setVisibility(View.VISIBLE);
                                                                try {
                                                                    Constant.loadSimpleImage(linkImg2.getContext(),
                                                                            linkPreviewModel.getFavIcon(),
                                                                            linkImg2,
                                                                            R.drawable.link_fav,
                                                                            R.drawable.link_fav);
                                                                } catch (Exception e) {
                                                                }
                                                            }

                                                            if (linkPreviewModel.getFavIcon().equals("") && linkPreviewModel.getDescription().equals("") && linkPreviewModel.getImage_url().equals("")) {
                                                                linkImg.setVisibility(View.VISIBLE);
                                                            }

                                                            linkTitle.setText(linkPreviewModel.getTitle());
                                                            link.setText(linkPreviewModel.getUrl());
                                                            link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.d("RICHLINK", "onError: " + e.getMessage());
                                        }
                                    });

                                    richPreview.getPreview(model.getMessage());
                                } catch (Exception ignored) {
                                    Log.d("TAG", "vsdvdsvs: " + ignored.getMessage());
                                }
                            } else {
                                sendMessage.setVisibility(View.VISIBLE);
                                richLinkViewLyt.setVisibility(View.GONE);
                            }

                            senderImg.setVisibility(View.GONE);
                            docLyt.setVisibility(View.GONE);
                            senderVideo.setVisibility(View.GONE);
                            sendervideoLyt.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.GONE);
                            miceContainer.setVisibility(View.GONE);

// todo readmore
                            if (model.getMessage().length() >= 200) {
                                readMore.setVisibility(View.VISIBLE);
                                sendMessage.setText(model.getMessage().substring(0, 200));
                            } else {
                                readMore.setVisibility(View.GONE);
                                sendMessage.setText(model.getMessage());
                            }

                            if (!model.getCaption().equals("")) {
                                captionText.setVisibility(View.VISIBLE);
                                captionText.setText(model.getCaption());
                            } else {
                                captionText.setVisibility(View.GONE);
                            }
                            LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);


                            deleteLyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                    // Print all group members before deleting
                                    printAllGroupMembers();

                                    database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                    Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                        @Override
                                                        public void onMembersReceived(ArrayList<members> groupMembers) {
                                                            if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                // Use background thread for smooth deletion
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (int i = 0; i < groupMembers.size(); i++) {
                                                                            members member = groupMembers.get(i);
                                                                            final int memberIndex = i;

                                                                            // Extract member UID
                                                                            String memberUid = extractMemberUid(member);

                                                                            if (memberUid != null && !memberUid.isEmpty()) {
                                                                                // Delete from CHAT database for each member using memberUid
                                                                                database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                            }
                                                                                        });

                                                                                // Call delete API for individual chatting
                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                // Add delay between operations for smooth execution
                                                                                try {
                                                                                    Thread.sleep(100);
                                                                                } catch (InterruptedException e) {
                                                                                    Thread.currentThread().interrupt();
                                                                                }
                                                                            }
                                                                        }

                                                                        // After all members processed, complete the deletion
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // Delete from local SQLite database
                                                                                try {
                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                } catch (Exception e) {
                                                                                    throw new RuntimeException(e);
                                                                                }

                                                                                // Remove item from UI
                                                                                removeItem(holder.getAdapterPosition());

                                                                                // Dismiss the delete dialog
                                                                                dismissDeleteDialog();

                                                                                Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();

                                                            } else {
                                                                Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                // Fallback to basic deletion if no members
                                                                database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                try {
                                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                } catch (Exception e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                removeItem(holder.getAdapterPosition());

                                                                                                // Dismiss the delete dialog
                                                                                                dismissDeleteDialog();

                                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            });


                            return true;
                        }
                    });

                } else
                if (model.getDataType().equals(Constant.contact)) {
                    ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                    ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).contactContainer.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                    ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                    if (!model.getCaption().equals("")) {
                        ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).captionText.setText(model.getCaption());

                    } else {
                        ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                    }
                    ((senderViewHolder) holder).cName.setText(model.getName());
                    ((senderViewHolder) holder).cPhone.setText(model.getPhone());
                    String text = model.getName();
                    String[] words = text.split(" ");
                    String firstWord = words[0];
                    ((senderViewHolder) holder).firstText.setText(firstWord);
                    ((senderViewHolder) holder).miceContainer.setVisibility(View.GONE);

                    //onclick listener for viewContact

                    ((senderViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((senderViewHolder) holder).viewContact.performClick();
                        }
                    });
                    ((senderViewHolder) holder).viewContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //after click on viewcontact

                            Constant.bottomsheetContact(mContext, R.layout.view_contact_btmsheet_lyt);
                            Constant.bottomSheetDialogContact.show();
                            TextView createContact = Constant.bottomSheetDialogContact.findViewById(R.id.createContact);
                            TextView existingContact = Constant.bottomSheetDialogContact.findViewById(R.id.existingContact);
                            LinearLayout cancel = Constant.bottomSheetDialogContact.findViewById(R.id.cancel);

                            createContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.bottomSheetDialogContact.dismiss();
                                    Constant.bottomsheetContact(mContext, R.layout.create_contact_layout_bottom);
                                    Constant.bottomSheetDialogContact.show();
                                    TextView cancel = Constant.bottomSheetDialogContact.findViewById(R.id.cancel);
                                    TextView save = Constant.bottomSheetDialogContact.findViewById(R.id.save);
                                    TextView mobile = Constant.bottomSheetDialogContact.findViewById(R.id.mobile);
                                    EditText firstname = Constant.bottomSheetDialogContact.findViewById(R.id.firstname);
                                    EditText lastName = Constant.bottomSheetDialogContact.findViewById(R.id.lastName);
                                    EditText phone = Constant.bottomSheetDialogContact.findViewById(R.id.phoneNumber);

                                    try {
                                        Constant.getSfFuncion(mContext);
                                        themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                        tintList = ColorStateList.valueOf(Color.parseColor(themColor));

                                        cancel.setTextColor(Color.parseColor(themColor));
                                        save.setTextColor(Color.parseColor(themColor));
                                        mobile.setTextColor(Color.parseColor(themColor));

                                    } catch (Exception ignored) {

                                    }

                                    String[] nameParts = model.getName().split(" ", 2);

                                    // Assuming the first part is the first name and the second part is the last name
                                    String firstNameString = nameParts[0];
                                    firstname.setText(firstNameString);
                                    try {
                                        String lastNameString = nameParts[1];
                                        lastName.setText(lastNameString);
                                    } catch (Exception ignored) {
                                    }


                                    phone.setText(model.getPhone());

                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Constant.bottomSheetDialogContact.dismiss();
                                        }
                                    });

                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // Check for permission before accessing contacts
                                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS)
                                                    != PackageManager.PERMISSION_GRANTED) {
                                                // Permission is not granted, request it
                                                ActivityCompat.requestPermissions(activity,
                                                        new String[]{Manifest.permission.WRITE_CONTACTS},
                                                        1);
                                            } else {


                                                ArrayList<ContentProviderOperation> operations = new ArrayList<>();
                                                operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                                        .build());

                                                operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstname.getText().toString() + " " + lastName.getText().toString())
                                                        .build());

                                                operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getText().toString())
                                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                                        .build());

                                                try {
                                                    mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
                                                    Constant.bottomSheetDialogContact.dismiss();
                                                } catch (RemoteException |
                                                         OperationApplicationException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                    });

                                }
                            });
//                                existingContact.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//
//
//                                        Constant.bottomSheetDialogContact.dismiss();
//                                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                                        ((Activity) mContext).startActivityForResult(intent, 7185);
//
//                                        phone2Contact.setText(model.getPhone());
//
//
//                                    }
//                                });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constant.bottomSheetDialogContact.dismiss();
                                }
                            });


                        }
                    });

                    ((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            float touchX = location[0];
                            float touchY = location[1];


                            BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                            BlurHelper.dialogLayoutColor.show();
                            RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                            // Use boundary-aware positioning
                            BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                            TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                            TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                            TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                            TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                            TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                            TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                            TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                            TextView cName = BlurHelper.dialogLayoutColor.findViewById(R.id.cName);
                            TextView cPhone = BlurHelper.dialogLayoutColor.findViewById(R.id.cPhone);
                            TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                            TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                            TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                            TextView firstText = BlurHelper.dialogLayoutColor.findViewById(R.id.firstText);
                            RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                            RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                            RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                            ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                            ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                            ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                            ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                            ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                            LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                            AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                            LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                            LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                            LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                            copy.setVisibility(View.GONE);
                            LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                            LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                            LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                            View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                            View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                            View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                            View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                            CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                            TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                            FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                            AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                            LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                            LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                            int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                // Dark mode is active
                                Constant.getSfFuncion(mContext);
                                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                ColorStateList tintList;

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#00A3E9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#7adf2a")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#ec0001")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#16f3ff")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#FF8A00")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#7F7F7F")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#D9B845")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#346667")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#9846D9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#A81010")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }
                                } catch (Exception ignored) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    MainSenderBox.setBackgroundTintList(tintList);
                                    richBox.setBackgroundTintList(tintList);


                                }


                            } else {
                                tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                            }

                            // todo theme
                            try {

                                Constant.getSfFuncion(mContext);
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#00A3E9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7adf2a")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#ec0001")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#16f3ff")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#FF8A00")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7F7F7F")) {


                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    } else if (themColor.equals("#D9B845")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#346667")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#9846D9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#A81010")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    }
                                } catch (Exception ignored) {

                                    viewnew.setBackgroundTintList(tintList);
                                    readMore.setTextColor(Color.parseColor(themColor));

                                }


                            } catch (Exception ignored) {
                            }
                            Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                            cardview.startAnimation(animationdd);
                            sendTime.setText(model.getTime());

                            // main code from here
                            Log.d("TAG444", "com");
                            Log.d("Mdid", model.getModelId());
                            readMore.setVisibility(View.GONE);
                            richLinkViewLyt.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.VISIBLE);
                            sendMessage.setVisibility(View.GONE);
                            senderImg.setVisibility(View.GONE);
                            docLyt.setVisibility(View.GONE);
                            senderVideo.setVisibility(View.GONE);
                            sendervideoLyt.setVisibility(View.GONE);
                            if (!model.getCaption().equals("")) {
                                captionText.setVisibility(View.VISIBLE);
                                captionText.setText(model.getCaption());
                            } else {
                                captionText.setVisibility(View.GONE);
                            }
                            cName.setText(model.getName());
                            cPhone.setText(model.getPhone());
                            String text = model.getName();
                            String[] words = text.split(" ");
                            String firstWord = words.length > 0 ? words[0] : "";
                            firstText.setText(firstWord);
                            miceContainer.setVisibility(View.GONE);

// onclick listener for viewContact

                            LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                            deleteLyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                    // Print all group members before deleting
                                    printAllGroupMembers();

                                    database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                    Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                        @Override
                                                        public void onMembersReceived(ArrayList<members> groupMembers) {
                                                            if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                // Use background thread for smooth deletion
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (int i = 0; i < groupMembers.size(); i++) {
                                                                            members member = groupMembers.get(i);
                                                                            final int memberIndex = i;

                                                                            // Extract member UID
                                                                            String memberUid = extractMemberUid(member);

                                                                            if (memberUid != null && !memberUid.isEmpty()) {
                                                                                // Delete from CHAT database for each member using memberUid
                                                                                database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                            }
                                                                                        });

                                                                                // Call delete API for individual chatting
                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                // Add delay between operations for smooth execution
                                                                                try {
                                                                                    Thread.sleep(100);
                                                                                } catch (InterruptedException e) {
                                                                                    Thread.currentThread().interrupt();
                                                                                }
                                                                            }
                                                                        }

                                                                        // After all members processed, complete the deletion
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // Delete from local SQLite database
                                                                                try {
                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                } catch (Exception e) {
                                                                                    throw new RuntimeException(e);
                                                                                }

                                                                                // Remove item from UI
                                                                                removeItem(holder.getAdapterPosition());

                                                                                // Dismiss the delete dialog
                                                                                dismissDeleteDialog();

                                                                                Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();

                                                            } else {
                                                                Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                // Fallback to basic deletion if no members
                                                                database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                try {
                                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                } catch (Exception e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                removeItem(holder.getAdapterPosition());

                                                                                                // Dismiss the delete dialog
                                                                                                dismissDeleteDialog();

                                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                            return true;
                        }
                    });

                } else
                if (model.getDataType().equals(Constant.voiceAudio)) {
                    ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                    ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                    ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).miceContainer.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).audioDownloadControls.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).miceTiming.setVisibility(View.VISIBLE);

                    File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                    if (!audiosDir.exists()) audiosDir.mkdirs();
                    File targetFile = new File(audiosDir, model.getFileName());



                    ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                    ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                    ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                    ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                    if (!model.getCaption().equals("")) {
                        ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).captionText.setText(model.getCaption());

                    } else {
                        ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                    }

                    ((senderViewHolder) holder).miceTiming.setText(model.getMiceTiming());
                    System.out.println("timing and url" + model.getMiceTiming() + model.getMicPhoto());

                    ((senderViewHolder) holder).micePlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("micePlayDebug", "micePlay clicked!");

                            // Get audio details from the model
                            String audioUrl = model.getDocument(); // URL or local path
                            String profileImageUrl = model.getMicPhoto() != null ? model.getMicPhoto() : "";
                            String songTitle = model.getFileName() != null ? model.getFileName() : "Audio Message";
                            String localFilePath = null;

                            // Check for local file first
                            File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                            Log.d("AudioDebug", "Checking audios directory: " + audiosDir.getAbsolutePath());
                            Log.d("AudioDebug", "Directory exists: " + audiosDir.exists() + ", isDirectory: " + audiosDir.isDirectory());

                            if (audiosDir.exists() && audiosDir.isDirectory()) {
                                // List all files in the directory for debugging
                                File[] files = audiosDir.listFiles();
                                if (files != null) {
                                    Log.d("AudioDebug", "Files in audios directory: " + files.length);
                                    for (File file : files) {
                                        Log.d("AudioDebug", "File: " + file.getName() + " (size: " + file.length() + " bytes)");
                                    }
                                }

                                File localFile = new File(audiosDir, model.getFileName());
                                Log.d("AudioDebug", "Looking for file: " + model.getFileName());
                                Log.d("AudioDebug", "Full path: " + localFile.getAbsolutePath());

                                if (localFile.exists() && localFile.isFile() && localFile.length() > 0) {
                                    localFilePath = localFile.getAbsolutePath();
                                    // Use file:// protocol for local files to ensure proper MediaPlayer handling
                                    audioUrl = "file://" + localFilePath;
                                    Log.d("AudioDebug", "✅ Local file found and valid: " + localFilePath);
                                    Log.d("AudioDebug", "Audio URL with file://: " + audioUrl);
                                    Log.d("AudioDebug", "File size: " + localFile.length() + " bytes");
                                } else {
                                    Log.d("AudioDebug", "❌ Local file not found or invalid: " + localFile.getAbsolutePath());
                                    Log.d("AudioDebug", "File exists: " + localFile.exists() + ", isFile: " + localFile.isFile() + ", length: " + (localFile.exists() ? localFile.length() : "N/A"));

                                    // Try alternative paths
                                    String[] alternativePaths = {
                                            "/storage/emulated/0/Android/data/com.Appzia.enclosure/files/Documents/Enclosure/Media/Audios",
                                            "/storage/emulated/0/Android/data/com.Appzia.enclosure/files/Enclosure/Media/Audios",
                                            Environment.getExternalStorageDirectory() + "/Enclosure/Media/Audios"
                                    };

                                    for (String altPath : alternativePaths) {
                                        File altDir = new File(altPath);
                                        if (altDir.exists() && altDir.isDirectory()) {
                                            File altFile = new File(altDir, model.getFileName());
                                            if (altFile.exists() && altFile.isFile() && altFile.length() > 0) {
                                                localFilePath = altFile.getAbsolutePath();
                                                audioUrl = "file://" + localFilePath;
                                                Log.d("AudioDebug", "✅ Found file in alternative path: " + localFilePath);
                                                Log.d("AudioDebug", "File size: " + altFile.length() + " bytes");
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                Log.d("AudioDebug", "❌ Audios directory not found: " + audiosDir.getAbsolutePath());
                            }

                            Log.d("AudioDebug", "Final audioUrl: " + audioUrl);
                            Log.d("AudioDebug", "Final localFilePath: " + localFilePath);

                            // Start foreground service for notification
                            Intent serviceIntent = new Intent(mContext, AudioPlaybackService.class);
                            serviceIntent.putExtra("audioUrl", audioUrl);
                            serviceIntent.putExtra("profileImageUrl", profileImageUrl);
                            serviceIntent.putExtra("songTitle", songTitle);
                            serviceIntent.putExtra("localFilePath", localFilePath);
                            serviceIntent.putExtra("modelId", model.getModelId());
                            serviceIntent.putExtra("position", holder.getAdapterPosition());
                            ContextCompat.startForegroundService(mContext, serviceIntent);

                            // Show the bottom sheet
                            try {
                                if (mContext instanceof AppCompatActivity) {
                                    Log.d("AudioDebug", "Showing MusicPlayerBottomSheet...");
                                    MusicPlayerBottomSheetGroup bottomSheet = MusicPlayerBottomSheetGroup.newInstance(audioUrl, profileImageUrl, songTitle, grpIdKey, name, caption);

                                    if (bottomSheet != null) {
                                        bottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), MusicPlayerBottomSheetGroup.TAG);
                                        Log.d("AudioDebug", "✅ MusicPlayerBottomSheet shown successfully");
                                    } else {
                                        Log.e("AudioDebug", "❌ Failed to create MusicPlayerBottomSheet instance");
                                    }
                                } else {
                                    Log.e("AudioDebug", "❌ mContext is not an AppCompatActivity: " + mContext.getClass().getSimpleName());
                                    Toast.makeText(mContext, "Unable to show player: Activity not compatible", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.e("AudioDebug", "❌ Error showing MusicPlayerBottomSheet: " + e.getMessage(), e);
                                Toast.makeText(mContext, "Error showing player: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    ((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            int[] location = new int[2];
                            v.getLocationOnScreen(location);
                            float touchX = location[0];
                            float touchY = location[1];


                            BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                            BlurHelper.dialogLayoutColor.show();
                            RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                            // Use boundary-aware positioning
                            BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                            TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                            TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                            TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                            TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                            TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                            TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                            TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                            TextView cName = BlurHelper.dialogLayoutColor.findViewById(R.id.cName);
                            TextView cPhone = BlurHelper.dialogLayoutColor.findViewById(R.id.cPhone);
                            TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                            TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                            TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                            TextView firstText = BlurHelper.dialogLayoutColor.findViewById(R.id.firstText);
                            RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                            RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                            RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                            ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                            ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                            ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                            ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                            ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                            LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                            AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                            LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                            LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                            LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                            copy.setVisibility(View.GONE);
                            LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                            LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                            LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                            View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                            View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                            View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                            View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                            CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                            TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                            FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                            AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                            LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                            LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                            LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                            LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                            CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                            LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                            TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                            LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                            CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                            CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                            ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                            TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                            ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                            TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                            RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                            FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                            ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                            TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                            ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                            RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                            ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                            TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                            FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                            ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                            CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                            LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                            TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                            TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                            TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);

                            int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                // Dark mode is active
                                Constant.getSfFuncion(mContext);
                                String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                ColorStateList tintList;

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#00A3E9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#7adf2a")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#ec0001")) {

                                        tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#16f3ff")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#FF8A00")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#7F7F7F")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#D9B845")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#346667")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else if (themColor.equals("#9846D9")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    } else if (themColor.equals("#A81010")) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);

                                    } else {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }
                                } catch (Exception ignored) {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                    MainSenderBox.setBackgroundTintList(tintList);
                                    richBox.setBackgroundTintList(tintList);


                                }


                            } else {
                                tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                            }

                            // todo theme
                            try {

                                Constant.getSfFuncion(mContext);
                                themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                try {
                                    if (themColor.equals("#ff0080")) {
                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#00A3E9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7adf2a")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#ec0001")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#16f3ff")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#FF8A00")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#7F7F7F")) {


                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    } else if (themColor.equals("#D9B845")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#346667")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#9846D9")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else if (themColor.equals("#A81010")) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    } else {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));


                                    }
                                } catch (Exception ignored) {

                                    viewnew.setBackgroundTintList(tintList);
                                    readMore.setTextColor(Color.parseColor(themColor));

                                }


                            } catch (Exception ignored) {
                            }
                            Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                            cardview.startAnimation(animationdd);
                            sendTime.setText(model.getTime());

                            // main logic from here
                            Log.d("TAG444", "com");
                            Log.d("Mdid", model.getModelId());
                            readMore.setVisibility(View.GONE);
                            richLinkViewLyt.setVisibility(View.GONE);
                            senderVideo.setVisibility(View.GONE);
                            sendervideoLyt.setVisibility(View.GONE);
                            miceContainer.setVisibility(View.VISIBLE);
                            audioDownloadControls.setVisibility(View.VISIBLE);
                            miceTiming.setVisibility(View.VISIBLE);

                            File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                            if (!audiosDir.exists()) audiosDir.mkdirs();
                            File targetFile = new File(audiosDir, model.getFileName());

                            if (targetFile.exists()) {
                                downlaodAudio.setVisibility(View.GONE);
                                progressBarAudio.setVisibility(View.GONE);
                                downloadPercentageAudioSender.setVisibility(View.GONE);
                                pauseButtonAudioSender.setVisibility(View.GONE);
                            } else {
                                downlaodAudio.setVisibility(View.VISIBLE);
                                progressBarAudio.setVisibility(View.GONE);
                                downloadPercentageAudioSender.setVisibility(View.GONE);
                                pauseButtonAudioSender.setVisibility(View.GONE);

                            }

                            sendMessage.setVisibility(View.GONE);
                            senderImg.setVisibility(View.GONE);
                            docLyt.setVisibility(View.GONE);
                            contactContainer.setVisibility(View.GONE);
                            if (!model.getCaption().equals("")) {
                                captionText.setVisibility(View.VISIBLE);
                                captionText.setText(model.getCaption());
                            } else {
                                captionText.setVisibility(View.GONE);
                            }

                            miceTiming.setText(model.getMiceTiming());
                            System.out.println("timing and url" + model.getMiceTiming() + model.getMicPhoto());

                            LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                            deleteLyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                    // Print all group members before deleting
                                    printAllGroupMembers();

                                    database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                    Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                        @Override
                                                        public void onMembersReceived(ArrayList<members> groupMembers) {
                                                            if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                // Use background thread for smooth deletion
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        for (int i = 0; i < groupMembers.size(); i++) {
                                                                            members member = groupMembers.get(i);
                                                                            final int memberIndex = i;

                                                                            // Extract member UID
                                                                            String memberUid = extractMemberUid(member);

                                                                            if (memberUid != null && !memberUid.isEmpty()) {
                                                                                // Delete from CHAT database for each member using memberUid
                                                                                database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                            }
                                                                                        });

                                                                                // Call delete API for individual chatting
                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                // Add delay between operations for smooth execution
                                                                                try {
                                                                                    Thread.sleep(100);
                                                                                } catch (InterruptedException e) {
                                                                                    Thread.currentThread().interrupt();
                                                                                }
                                                                            }
                                                                        }

                                                                        // After all members processed, complete the deletion
                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                // Delete from local SQLite database
                                                                                try {
                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                } catch (Exception e) {
                                                                                    throw new RuntimeException(e);
                                                                                }

                                                                                // Remove item from UI
                                                                                removeItem(holder.getAdapterPosition());

                                                                                Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                            }
                                                                        });
                                                                    }
                                                                }).start();

                                                            } else {
                                                                Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                // Fallback to basic deletion if no members
                                                                database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                        .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void unused) {
                                                                                                try {
                                                                                                    new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                } catch (Exception e) {
                                                                                                    throw new RuntimeException(e);
                                                                                                }
                                                                                                removeItem(holder.getAdapterPosition());
                                                                                                Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                            return true;
                        }
                    });


                    File docsDir;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        docsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
                    } else {
                        docsDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Audios");
                    }
                    if (!docsDir.exists()) docsDir.mkdirs();
                    File localDoc = new File(docsDir, model.getFileName());
                    boolean docExists = localDoc.exists();
                    ((senderViewHolder) holder).audioDownloadControls.setVisibility(View.VISIBLE);
                    ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                    ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);

                    ((senderViewHolder) holder).downlaodAudio.setVisibility(docExists ? View.GONE : View.VISIBLE);

                    ((senderViewHolder) holder).downlaodAudio.setOnClickListener(v -> {

                        startSenderAudioDownloadWithProgress(holder, model);
                    });


                } else {
                    ((senderViewHolder) holder).senderImgBunchLyt.setVisibility(View.GONE);
                    List<String> musicExtensions = Arrays.asList(
                            "mp3", "wav", "flac", "aac", "ogg", "oga", "m4a", "wma", "alac", "aiff"
                    );

                    String ext = model.getExtension();
                    if (ext != null && musicExtensions.contains(ext.toLowerCase())) {
                        ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                        ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                        ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                        ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                        ((senderViewHolder) holder).miceContainer.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).audioDownloadControls.setVisibility(View.VISIBLE);

                        File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                        if (!audiosDir.exists()) audiosDir.mkdirs();
                        File targetFile = new File(audiosDir, model.getFileName());

                        if (targetFile.exists()) {
                            ((senderViewHolder) holder).downlaodAudio.setVisibility(View.GONE);
                            ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                            ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
                            ((senderViewHolder) holder).pauseButtonAudioSender.setVisibility(View.GONE);
                        } else {
                            ((senderViewHolder) holder).downlaodAudio.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                            ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
                            ((senderViewHolder) holder).pauseButtonAudioSender.setVisibility(View.GONE);
                            ((senderViewHolder) holder).downlaodAudio.setOnClickListener(v -> {
                                startSenderAudioDownloadWithProgressXDocument(holder, model);
                            });
                        }

                        ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                        ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                        ((senderViewHolder) holder).docLyt.setVisibility(View.GONE);
                        ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                        if (!model.getCaption().equals("")) {
                            ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).captionText.setText(model.getCaption());

                        } else {
                            ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                        }

                        ((senderViewHolder) holder).miceTiming.setText(model.getMiceTiming());
                        System.out.println("timing and url" + model.getMiceTiming() + model.getMicPhoto());

                        ((senderViewHolder) holder).micePlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String audioUrl = model.getDocument();
                                String profileImageUrl = model.getMicPhoto() != null ? model.getMicPhoto() : "";
                                String songTitle = model.getFileName() != null ? model.getFileName() : "Audio Message";
                                String localFilePath = null;

                                // Prefer cacheDir (matches our download location)
                                File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");

                                if (audiosDir != null) {
                                    String altPath = new File(audiosDir, model.getFileName()).getAbsolutePath();
                                    if (doesFileExist(altPath)) {
                                        localFilePath = altPath;
                                        audioUrl = altPath;
                                        Log.e("3456yhjkldxwaxa", "onClick: " + audioUrl);
                                    }
                                }

                                Intent serviceIntent = new Intent(mContext, AudioPlaybackServiceGroup.class);
                                serviceIntent.putExtra("audioUrl", audioUrl);
                                serviceIntent.putExtra("profileImageUrl", profileImageUrl);
                                serviceIntent.putExtra("songTitle", songTitle);
                                serviceIntent.putExtra("localFilePath", localFilePath);
                                serviceIntent.putExtra("modelId", model.getModelId());
                                serviceIntent.putExtra("position", holder.getAdapterPosition());
                                serviceIntent.putExtra("grpIdKey", grpIdKey);
                                serviceIntent.putExtra("captionKey", caption);
                                serviceIntent.putExtra("nameKey", name);
                                ContextCompat.startForegroundService(mContext, serviceIntent);

                                // Show bottom sheet UI
                                if (mContext instanceof AppCompatActivity) {
                                    MusicPlayerBottomSheetGroup bottomSheet = MusicPlayerBottomSheetGroup.newInstance(audioUrl, profileImageUrl, songTitle, grpIdKey, name, caption);
                                    bottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), MusicPlayerBottomSheetGroup.TAG);
                                } else {
                                    Log.e("MusicPlayer", "mContext is not an AppCompatActivity: " + mContext.getClass().getSimpleName());
                                }
                            }
                        });

                        ((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                int[] location = new int[2];
                                v.getLocationOnScreen(location);
                                float touchX = location[0];
                                float touchY = location[1];


                                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                                BlurHelper.dialogLayoutColor.show();
                                RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                                // Use boundary-aware positioning
                                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                                TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                                TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                                TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                                TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                                TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                                TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                                TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                                TextView cName = BlurHelper.dialogLayoutColor.findViewById(R.id.cName);
                                TextView cPhone = BlurHelper.dialogLayoutColor.findViewById(R.id.cPhone);
                                TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                                TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                                TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                                TextView firstText = BlurHelper.dialogLayoutColor.findViewById(R.id.firstText);
                                RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                                RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                                RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                                ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                                ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                                ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                                ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                                ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                                LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                                AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                                LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                                LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                                LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                                copy.setVisibility(View.GONE);
                                LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                                LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                                LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                                View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                                View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                                View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                                View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                                CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                                TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                                FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                                AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                                LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                                LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                                LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                                LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                                CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                                LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                                TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                                LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                                CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                                CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                                ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                                TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                                ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                                TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                                RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                                FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                                ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                                TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                                ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                                RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                                ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                                TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                                FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                                ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                                CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                                LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                                TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                                TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                                TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);

                                int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                    // Dark mode is active
                                    Constant.getSfFuncion(mContext);
                                    String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                    ColorStateList tintList;

                                    try {
                                        if (themColor.equals("#ff0080")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#00A3E9")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#7adf2a")) {

                                            tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#ec0001")) {

                                            tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#16f3ff")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#FF8A00")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#7F7F7F")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#D9B845")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#346667")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#9846D9")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#A81010")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        }
                                    } catch (Exception ignored) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }


                                } else {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                    MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                                }

                                // todo theme
                                try {

                                    Constant.getSfFuncion(mContext);
                                    themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                    tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                    //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                    try {
                                        if (themColor.equals("#ff0080")) {
                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#00A3E9")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#7adf2a")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#ec0001")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#16f3ff")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#FF8A00")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#7F7F7F")) {


                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));

                                        } else if (themColor.equals("#D9B845")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#346667")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#9846D9")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#A81010")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        }
                                    } catch (Exception ignored) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    }


                                } catch (Exception ignored) {
                                }
                                Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                                cardview.startAnimation(animationdd);
                                sendTime.setText(model.getTime());

                                // main logic from here
                                Log.d("TAG444", "com");
                                Log.d("Mdid", model.getModelId());
                                readMore.setVisibility(View.GONE);
                                richLinkViewLyt.setVisibility(View.GONE);
                                senderVideo.setVisibility(View.GONE);
                                sendervideoLyt.setVisibility(View.GONE);
                                miceContainer.setVisibility(View.VISIBLE);
                                audioDownloadControls.setVisibility(View.GONE);

                                File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                if (!audiosDir.exists()) audiosDir.mkdirs();
                                File targetFile = new File(audiosDir, model.getFileName());

                                if (targetFile.exists()) {
                                    downlaodAudio.setVisibility(View.GONE);
                                    progressBarAudio.setVisibility(View.GONE);
                                    downloadPercentageAudioSender.setVisibility(View.GONE);
                                    pauseButtonAudioSender.setVisibility(View.GONE);
                                } else {
                                    downlaodAudio.setVisibility(View.GONE);
                                    progressBarAudio.setVisibility(View.GONE);
                                    downloadPercentageAudioSender.setVisibility(View.GONE);
                                    pauseButtonAudioSender.setVisibility(View.GONE);

                                }

                                sendMessage.setVisibility(View.GONE);
                                senderImg.setVisibility(View.GONE);
                                docLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.GONE);
                                if (!model.getCaption().equals("")) {
                                    captionText.setVisibility(View.VISIBLE);
                                    captionText.setText(model.getCaption());
                                } else {
                                    captionText.setVisibility(View.GONE);
                                }

                                miceTiming.setText(model.getMiceTiming());
                                System.out.println("timing and url" + model.getMiceTiming() + model.getMicPhoto());

                                LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                                deleteLyt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                        // Print all group members before deleting
                                        printAllGroupMembers();

                                        database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                                .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                        Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                            @Override
                                                            public void onMembersReceived(ArrayList<members> groupMembers) {
                                                                if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                    Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                    // Use background thread for smooth deletion
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            for (int i = 0; i < groupMembers.size(); i++) {
                                                                                members member = groupMembers.get(i);
                                                                                final int memberIndex = i;

                                                                                // Extract member UID
                                                                                String memberUid = extractMemberUid(member);

                                                                                if (memberUid != null && !memberUid.isEmpty()) {
                                                                                    // Delete from CHAT database for each member using memberUid
                                                                                    database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                                }
                                                                                            });

                                                                                    // Call delete API for individual chatting
                                                                                    Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                    // Add delay between operations for smooth execution
                                                                                    try {
                                                                                        Thread.sleep(100);
                                                                                    } catch (InterruptedException e) {
                                                                                        Thread.currentThread().interrupt();
                                                                                    }
                                                                                }
                                                                            }

                                                                            // After all members processed, complete the deletion
                                                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    // Delete from local SQLite database
                                                                                    try {
                                                                                        new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                    } catch (Exception e) {
                                                                                        throw new RuntimeException(e);
                                                                                    }

                                                                                    // Remove item from UI
                                                                                    removeItem(holder.getAdapterPosition());

                                                                                    // Dismiss the delete dialog
                                                                                    dismissDeleteDialog();

                                                                                    Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                                }
                                                                            });
                                                                        }
                                                                    }).start();

                                                                } else {
                                                                    Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                    // Fallback to basic deletion if no members
                                                                    database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    try {
                                                                                                        new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                    } catch (Exception e) {
                                                                                                        throw new RuntimeException(e);
                                                                                                    }
                                                                                                    removeItem(holder.getAdapterPosition());

                                                                                                    // Dismiss the delete dialog
                                                                                                    dismissDeleteDialog();

                                                                                                    Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                                return true;
                            }
                        });


                        File docsDir;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            docsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                        } else {
                            docsDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                        }
                        if (!docsDir.exists()) docsDir.mkdirs();
                        File localDoc = new File(docsDir, model.getFileName());
                        boolean docExists = localDoc.exists();
                        ((senderViewHolder) holder).audioDownloadControls.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                        ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);

                        ((senderViewHolder) holder).downlaodAudio.setVisibility(docExists ? View.GONE : View.VISIBLE);

                        ((senderViewHolder) holder).downlaodAudio.setOnClickListener(v -> {

                            startSenderDocDownloadWithProgress(holder, model);
                        });

                    } else {


                        Log.d("TAG444", "com");
                        //for docs
                        ((senderViewHolder) holder).readMore.setVisibility(View.GONE);
                        ((senderViewHolder) holder).richLinkViewLyt.setVisibility(View.GONE);
                        ((senderViewHolder) holder).docLyt.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).sendMessage.setVisibility(View.GONE);
                        ((senderViewHolder) holder).senderImg.setVisibility(View.GONE);
                        ((senderViewHolder) holder).docName.setText(model.getFileName());
                        ((senderViewHolder) holder).extension.setText(model.getExtension());
                        ((senderViewHolder) holder).senderVideo.setVisibility(View.GONE);
                        ((senderViewHolder) holder).sendervideoLyt.setVisibility(View.GONE);
                        ((senderViewHolder) holder).contactContainer.setVisibility(View.GONE);
                        ((senderViewHolder) holder).miceContainer.setVisibility(View.GONE);

                        // Determine local documents dir
                        File docsDir;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            docsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                        } else {
                            docsDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                        }
                        if (!docsDir.exists()) docsDir.mkdirs();

                        File localDoc = new File(docsDir, model.getFileName());
                        boolean docExists = localDoc.exists();

                        ((senderViewHolder) holder).docDownloadControls.setVisibility(View.VISIBLE);
                        ((senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
                        ((senderViewHolder) holder).downloadPercentageDocSender.setVisibility(View.GONE);

                        ((senderViewHolder) holder).downlaodDoc.setVisibility(docExists ? View.GONE : View.VISIBLE);


                        ((senderViewHolder) holder).downlaodDoc.setOnClickListener(v -> {
                            startSenderDocDownloadWithProgress(holder, model);
                        });


                        if (model.getExtension().equalsIgnoreCase("pdf")) {
                            ((senderViewHolder) holder).pdfPreview.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).pdfcard.setVisibility(View.VISIBLE);

                            String localPreviewImagePath = getLocalPdfPreviewImagePath(model);
                            // Get the remote URL of the actual PDF document
                            String remotePdfUrl = getRemotePdfUrl(model);

                            // Load the PDF preview, handling caching logic internally
                            ImageView targetImageView = ((senderViewHolder) holder).pdfPreview;
                            ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                            loadPdfPreview(localPreviewImagePath, remotePdfUrl, ((senderViewHolder) holder).pdfPreview, parentLayout, position, model);

                            ((senderViewHolder) holder).docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.RED));


                        } else if (model.getExtension().equalsIgnoreCase("xls")) {
                            ((senderViewHolder) holder).pdfPreview.setVisibility(View.GONE);
                            ((senderViewHolder) holder).pdfcard.setVisibility(View.GONE);
                            String filePath = getFilePath(model);


                            ((senderViewHolder) holder).docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#107C3D")));

                        } else {
                            ((senderViewHolder) holder).pdfPreview.setVisibility(View.GONE);
                            ((senderViewHolder) holder).pdfcard.setVisibility(View.GONE);
                            ((senderViewHolder) holder).docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D8DE")));
                        }

                        if (!model.getCaption().equals("")) {
                            ((senderViewHolder) holder).captionText.setVisibility(View.VISIBLE);
                            ((senderViewHolder) holder).captionText.setText(model.getCaption());

                        } else {
                            ((senderViewHolder) holder).captionText.setVisibility(View.GONE);
                        }

                        ((senderViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                File customFolder;
                                String exactPath = null;
                                //android 10
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                    exactPath = customFolder.getAbsolutePath();
                                    Log.d("TAG", "exactPath: " + exactPath + "/" + model.getFileName());
                                } else {
                                    customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                                    exactPath = customFolder.getAbsolutePath();

                                }

                                if (doesFileExist(exactPath + "/" + model.getFileName())) {

                                    Intent intent = new Intent(mContext, show_document_screen.class);
                                    intent.putExtra("documentKey", model.getFileName());
                                    // nameKey should be the display/file name, not dataType
                                    intent.putExtra("nameKey", model.getFileName());
                                    intent.putExtra("sizeKey", model.getDocSize());
                                    intent.putExtra("extensionKey", model.getExtension());
                                    intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
                                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                } else {
//                                    Intent intent = new Intent(mContext, show_document_screen.class);
//                                    intent.putExtra("documentKey", model.getDocument());
//                                    // Use actual file name for UI and for local save target in document screen
//                                    intent.putExtra("nameKey", model.getFileName());
//                                    intent.putExtra("sizeKey", model.getDocSize());
//                                    intent.putExtra("extensionKey", model.getExtension());
//                                    intent.putExtra("viewHolderTypeKey", Constant.senderViewHolder);
//                                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                }
                            }
                        });

                        ((senderViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                int[] location = new int[2];
                                v.getLocationOnScreen(location);
                                float touchX = location[0];
                                float touchY = location[1];


                                BlurHelper.showDialogWithBlurBackground(mContext, R.layout.sender_long_press_group_dialogue);
                                BlurHelper.dialogLayoutColor.show();
                                RelativeLayout relativeLayout = BlurHelper.dialogLayoutColor.findViewById(R.id.relativelayout);
                                // Use boundary-aware positioning
                                BlurHelper.positionDialogWithinBounds(mContext, touchX, touchY, relativeLayout);

                                TextView sendMessage = BlurHelper.dialogLayoutColor.findViewById(R.id.sendMessage);
                                TextView sendTime = BlurHelper.dialogLayoutColor.findViewById(R.id.sendTime);
                                TextView linkActualUrl = BlurHelper.dialogLayoutColor.findViewById(R.id.linkActualUrl);
                                TextView link = BlurHelper.dialogLayoutColor.findViewById(R.id.link);
                                TextView linkDesc = BlurHelper.dialogLayoutColor.findViewById(R.id.linkDesc);
                                TextView captionText = BlurHelper.dialogLayoutColor.findViewById(R.id.captionText);
                                TextView linkTitle = BlurHelper.dialogLayoutColor.findViewById(R.id.linkTitle);
                                TextView cName = BlurHelper.dialogLayoutColor.findViewById(R.id.cName);
                                TextView cPhone = BlurHelper.dialogLayoutColor.findViewById(R.id.cPhone);
                                TextView forwarded = BlurHelper.dialogLayoutColor.findViewById(R.id.forwarded);
                                TextView repliedData = BlurHelper.dialogLayoutColor.findViewById(R.id.repliedData);
                                TextView miceTiming = BlurHelper.dialogLayoutColor.findViewById(R.id.miceTiming);
                                TextView extension = BlurHelper.dialogLayoutColor.findViewById(R.id.extension);
                                TextView docName = BlurHelper.dialogLayoutColor.findViewById(R.id.docName);
                                TextView firstText = BlurHelper.dialogLayoutColor.findViewById(R.id.firstText);
                                RelativeLayout richLinkViewLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.richLinkViewLyt);
                                RelativeLayout senderImgLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImgLyt);
                                RelativeLayout sendervideoLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.sendervideoLyt);
                                ImageView linkImg = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg);
                                ImageView linkImg2 = BlurHelper.dialogLayoutColor.findViewById(R.id.linkImg2);
                                ImageView miceUImage = BlurHelper.dialogLayoutColor.findViewById(R.id.miceUImage);
                                ImageView senderVideo = BlurHelper.dialogLayoutColor.findViewById(R.id.senderVideo);
                                ProgressBar progressBarImageview = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBar);
                                LinearProgressIndicator miceProgressbar = BlurHelper.dialogLayoutColor.findViewById(R.id.miceProgressbar);
                                AppCompatImageView senderImg = BlurHelper.dialogLayoutColor.findViewById(R.id.senderImg);
                                LinearLayout contactContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainer);
                                LinearLayout docLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.docLyt);
                                LinearLayout copy = BlurHelper.dialogLayoutColor.findViewById(R.id.copy);
                                copy.setVisibility(View.GONE);
                                LinearLayout progresslyt = BlurHelper.dialogLayoutColor.findViewById(R.id.progresslyt);
                                LinearLayout miceContainer = BlurHelper.dialogLayoutColor.findViewById(R.id.miceContainer);
                                LinearLayout replydatalyt = BlurHelper.dialogLayoutColor.findViewById(R.id.replydatalyt);

                                View viewnew = BlurHelper.dialogLayoutColor.findViewById(R.id.viewnew);
                                View replyDevider = BlurHelper.dialogLayoutColor.findViewById(R.id.replyDevider);
                                View viewbarlyt1 = BlurHelper.dialogLayoutColor.findViewById(R.id.viewbarlyt1);
                                View blur = BlurHelper.dialogLayoutColor.findViewById(R.id.blur);
                                CardView cardview = BlurHelper.dialogLayoutColor.findViewById(R.id.cardview);
                                TextView readMore = BlurHelper.dialogLayoutColor.findViewById(R.id.readMore);
                                FloatingActionButton downlaod = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaod);

                                AppCompatImageButton micePlay = BlurHelper.dialogLayoutColor.findViewById(R.id.micePlay);

                                LinearLayout richBox = BlurHelper.dialogLayoutColor.findViewById(R.id.richBox);
                                LinearLayout MainSenderBox = BlurHelper.dialogLayoutColor.findViewById(R.id.MainSenderBox);

                                LinearLayout replylyoutGlobal = BlurHelper.dialogLayoutColor.findViewById(R.id.replylyoutGlobal);
                                LinearLayout contactContainerReply = BlurHelper.dialogLayoutColor.findViewById(R.id.contactContainerReply);
                                CardView imgcardview = BlurHelper.dialogLayoutColor.findViewById(R.id.imgcardview);
                                LinearLayout replyTheme = BlurHelper.dialogLayoutColor.findViewById(R.id.replyTheme);
                                TextView replyYou = BlurHelper.dialogLayoutColor.findViewById(R.id.replyYou);
                                LinearLayout pageLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.pageLyt);
                                CardView musicReply = BlurHelper.dialogLayoutColor.findViewById(R.id.muciReply);
                                CardView miceReply = BlurHelper.dialogLayoutColor.findViewById(R.id.miceReply);
                                ImageView videoicon = BlurHelper.dialogLayoutColor.findViewById(R.id.videoicon);
                                TextView msgreplyText = BlurHelper.dialogLayoutColor.findViewById(R.id.msgreplyText);
                                ImageView imgreply = BlurHelper.dialogLayoutColor.findViewById(R.id.imgreply);
                                TextView firstTextReply = BlurHelper.dialogLayoutColor.findViewById(R.id.firstTextReply);
                                RelativeLayout audioDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.audioDownloadControls);
                                FloatingActionButton downlaodAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodAudio);
                                ProgressBar progressBarAudio = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarAudio);
                                TextView downloadPercentageAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageAudioSender);
                                ImageButton pauseButtonAudioSender = BlurHelper.dialogLayoutColor.findViewById(R.id.pauseButtonAudioSender);
                                RelativeLayout docDownloadControls = BlurHelper.dialogLayoutColor.findViewById(R.id.docDownloadControls);
                                ProgressBar progressBarDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.progressBarDoc);
                                TextView downloadPercentageDocSender = BlurHelper.dialogLayoutColor.findViewById(R.id.downloadPercentageDocSender);
                                FloatingActionButton downlaodDoc = BlurHelper.dialogLayoutColor.findViewById(R.id.downlaodDoc);
                                ImageView pdfPreview = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfPreview);
                                CardView pdfcard = BlurHelper.dialogLayoutColor.findViewById(R.id.pdfcard);
                                LinearLayout docFileIcon = BlurHelper.dialogLayoutColor.findViewById(R.id.docFileIcon);
                                TextView docSize = BlurHelper.dialogLayoutColor.findViewById(R.id.docSize);
                                TextView docSizeExtension = BlurHelper.dialogLayoutColor.findViewById(R.id.docSizeExtension);
                                TextView pageText = BlurHelper.dialogLayoutColor.findViewById(R.id.pageText);

                                int nightModeFlags = mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                                    // Dark mode is active
                                    Constant.getSfFuncion(mContext);
                                    String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                    ColorStateList tintList;

                                    try {
                                        if (themColor.equals("#ff0080")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#4D0026"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#00A3E9")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));

                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#7adf2a")) {

                                            tintList = ColorStateList.valueOf(Color.parseColor("#25430D"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#ec0001")) {

                                            tintList = ColorStateList.valueOf(Color.parseColor("#470000"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#16f3ff")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#05495D"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#FF8A00")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#663700"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#7F7F7F")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#2B3137"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#D9B845")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#413815"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#346667")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#1F3D3E"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else if (themColor.equals("#9846D9")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#2d1541"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        } else if (themColor.equals("#A81010")) {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#430706"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);

                                        } else {
                                            tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                            MainSenderBox.setBackgroundTintList(tintList);
                                            richBox.setBackgroundTintList(tintList);


                                        }
                                    } catch (Exception ignored) {
                                        tintList = ColorStateList.valueOf(Color.parseColor("#01253B"));
                                        MainSenderBox.setBackgroundTintList(tintList);
                                        richBox.setBackgroundTintList(tintList);


                                    }


                                } else {
                                    tintList = ColorStateList.valueOf(Color.parseColor("#011224"));
                                    MainSenderBox.setBackgroundTintList(tintList); // Replace #011224 with your hex color value
                                }

                                // todo theme
                                try {

                                    Constant.getSfFuncion(mContext);
                                    themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");
                                    tintList = ColorStateList.valueOf(Color.parseColor(themColor));


                                    //   binding.menuPoint.setBackgroundColor(Color.parseColor(themColor));

                                    try {
                                        if (themColor.equals("#ff0080")) {
                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#00A3E9")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#7adf2a")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#ec0001")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#16f3ff")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#FF8A00")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#7F7F7F")) {


                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));

                                        } else if (themColor.equals("#D9B845")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#346667")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#9846D9")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else if (themColor.equals("#A81010")) {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        } else {

                                            viewnew.setBackgroundTintList(tintList);
                                            readMore.setTextColor(Color.parseColor(themColor));


                                        }
                                    } catch (Exception ignored) {

                                        viewnew.setBackgroundTintList(tintList);
                                        readMore.setTextColor(Color.parseColor(themColor));

                                    }


                                } catch (Exception ignored) {
                                }
                                Animation animationdd = AnimationUtils.loadAnimation(mContext, R.anim.unfold_animation);
                                cardview.startAnimation(animationdd);
                                sendTime.setText(model.getTime());

                                // main logic from here
                                Log.d("TAG444", "com");
                                Log.d("TAG444", "com");
                                Log.d("Mdid", model.getModelId());
                                readMore.setVisibility(View.GONE);
                                richLinkViewLyt.setVisibility(View.GONE);
                                docLyt.setVisibility(View.VISIBLE);
                                sendMessage.setVisibility(View.GONE);
                                senderImg.setVisibility(View.GONE);
                                docName.setText(model.getFileName());
                                extension.setText(model.getExtension());
                                senderVideo.setVisibility(View.GONE);
                                sendervideoLyt.setVisibility(View.GONE);
                                contactContainer.setVisibility(View.GONE);
                                miceContainer.setVisibility(View.GONE);

// Determine local documents dir
                                File docsDir;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    docsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
                                } else {
                                    docsDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
                                }
                                if (!docsDir.exists()) docsDir.mkdirs();

                                File localDoc = new File(docsDir, model.getFileName());
                                boolean docExists = localDoc.exists();

                                docDownloadControls.setVisibility(View.GONE);
                                progressBarDoc.setVisibility(View.GONE);
                                downloadPercentageDocSender.setVisibility(View.GONE);

                                downlaodDoc.setVisibility(View.GONE);

// Click to download

                                if (model.getExtension().equalsIgnoreCase("pdf")) {
                                    pdfPreview.setVisibility(View.VISIBLE);
                                    pdfcard.setVisibility(View.VISIBLE);

                                    String localPreviewImagePath = getLocalPdfPreviewImagePath(model);
                                    // Get the remote URL of the actual PDF document
                                    String remotePdfUrl = getRemotePdfUrl(model);

                                    // Load the PDF preview, handling caching logic internally
                                    ImageView targetImageView = pdfPreview;
                                    ViewGroup parentLayout = (ViewGroup) targetImageView.getParent();
                                    loadPdfPreview(localPreviewImagePath, remotePdfUrl, pdfPreview, parentLayout, position, model);

                                    docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                                } else if (model.getExtension().equalsIgnoreCase("xls")) {
                                    pdfPreview.setVisibility(View.GONE);
                                    pdfcard.setVisibility(View.GONE);
                                    String filePath = getFilePath(model);

                                    docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#107C3D")));
                                } else {
                                    pdfPreview.setVisibility(View.GONE);
                                    pdfcard.setVisibility(View.GONE);
                                    docFileIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D6D8DE")));
                                }

                                if (!model.getCaption().equals("")) {
                                    captionText.setVisibility(View.VISIBLE);
                                    captionText.setText(model.getCaption());
                                } else {
                                    captionText.setVisibility(View.GONE);
                                }

                                LinearLayout deleteLyt = BlurHelper.dialogLayoutColor.findViewById(R.id.deletelyt);
                                deleteLyt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((senderViewHolder) holder).menu2.setVisibility(View.GONE);

                                        // Print all group members before deleting
                                        printAllGroupMembers();

                                        database.getReference().child(Constant.GROUPCHAT).child(model.getUid() + model.getReceiverUid())
                                                .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        // After GROUPCHAT deletion succeeds, fetch group members and delete for each
                                                        Webservice.get_group_members_for_adapter(grpIdKey, mContext, new Webservice.GroupMembersCallback() {
                                                            @Override
                                                            public void onMembersReceived(ArrayList<members> groupMembers) {
                                                                if (groupMembers != null && !groupMembers.isEmpty()) {
                                                                    Log.d("GroupMembers", "Starting delete for " + groupMembers.size() + " group members");

                                                                    // Use background thread for smooth deletion
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            for (int i = 0; i < groupMembers.size(); i++) {
                                                                                members member = groupMembers.get(i);
                                                                                final int memberIndex = i;

                                                                                // Extract member UID
                                                                                String memberUid = extractMemberUid(member);

                                                                                if (memberUid != null && !memberUid.isEmpty()) {
                                                                                    // Delete from CHAT database for each member using memberUid
                                                                                    database.getReference().child(Constant.CHAT).child(memberUid + model.getUid())
                                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    Log.d("GroupMembers", "Deleted from CHAT for member " + (memberIndex + 1) + ": " + memberUid);
                                                                                                }
                                                                                            });

                                                                                    // Call delete API for individual chatting
                                                                                    Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), memberUid);

                                                                                    // Add delay between operations for smooth execution
                                                                                    try {
                                                                                        Thread.sleep(100);
                                                                                    } catch (InterruptedException e) {
                                                                                        Thread.currentThread().interrupt();
                                                                                    }
                                                                                }
                                                                            }

                                                                            // After all members processed, complete the deletion
                                                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    // Delete from local SQLite database
                                                                                    try {
                                                                                        new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                    } catch (Exception e) {
                                                                                        throw new RuntimeException(e);
                                                                                    }

                                                                                    // Remove item from UI
                                                                                    removeItem(holder.getAdapterPosition());

                                                                                    Log.d("GroupMembers", "All group member deletions completed successfully");
                                                                                }
                                                                            });
                                                                        }
                                                                    }).start();

                                                                } else {
                                                                    Log.w("GroupMembers", "No group members found, proceeding with basic deletion");
                                                                    // Fallback to basic deletion if no members
                                                                    database.getReference().child(Constant.CHAT).child(model.getReceiverUid() + model.getUid())
                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    database.getReference().child(Constant.CHAT).child(model.getUid() + model.getReceiverUid())
                                                                                            .child(model.getModelId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    try {
                                                                                                        new DatabaseHelper(mContext).deleteIndividualChatting(model.getModelId());
                                                                                                    } catch (Exception e) {
                                                                                                        throw new RuntimeException(e);
                                                                                                    }
                                                                                                    removeItem(holder.getAdapterPosition());
                                                                                                    Webservice.delete_chatingindivisual(mContext, model.getModelId(), model.getUid(), model.getReceiverUid());
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                                return true;
                            }
                        });

                    }

                }


            } else {

            }
        } catch (Exception ignored) {
            //   Toast.makeText(mContext, ignored.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "onBindViewHolder: " + ignored.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return groupMessageList.size();
    }

    @Override
    public long getItemId(int position) {
        try {
            group_messageModel model = groupMessageList.get(position);
            if (model == null) return RecyclerView.NO_ID;
            String mid = model.getModelId();
            if (mid != null && !mid.isEmpty()) {
                return (mid.hashCode() & 0xffffffffL);
            }
            long ts = 0L;
            try {
                // group_messageModel timestamp method may differ; attempt reflection-like fallback
                ts = Long.parseLong(model.getTime() != null ? model.getTime().replaceAll("[^0-9]", "") : "0");
            } catch (Exception ignored) {
                ts = 0L;
            }
            String uid = model.getUid() == null ? "" : model.getUid();
            String rid = model.getReceiverUid() == null ? "" : model.getReceiverUid();
            String key = uid + "|" + rid + "|" + ts;
            return (key.hashCode() & 0xffffffffL);
        } catch (Exception ignored) {
            return RecyclerView.NO_ID;
        }
    }

    public void searchFilteredData(ArrayList<group_messageModel> filteredList) {
        groupMessageList = filteredList;
        notifyDataSetChanged();
    }

    public static class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView recMessage, recTime, docName, extension, cName, cPhone, firstText, receiverName;
        AppCompatImageView recImg;
        LinearLayout docLyt, contactContainer, viewContact;
        LinearLayout miceContainer;
        ImageView miceUImage;
        AppCompatImageButton micePlay;
        LinearProgressIndicator miceProgressbar;
        TextView miceTiming;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            recMessage = itemView.findViewById(R.id.recMessage);
            recTime = itemView.findViewById(R.id.recTime);
            recImg = itemView.findViewById(R.id.recImg);
            docName = itemView.findViewById(R.id.docName);
            docLyt = itemView.findViewById(R.id.docLyt);
            extension = itemView.findViewById(R.id.extension);
            contactContainer = itemView.findViewById(R.id.contactContainer);
            cName = itemView.findViewById(R.id.cName);
            cPhone = itemView.findViewById(R.id.cPhone);
            viewContact = itemView.findViewById(R.id.viewContact);
            firstText = itemView.findViewById(R.id.firstText);
            miceContainer = itemView.findViewById(R.id.miceContainer);
            miceUImage = itemView.findViewById(R.id.miceUImage);
            micePlay = itemView.findViewById(R.id.micePlay);
            miceProgressbar = itemView.findViewById(R.id.miceProgressbar);
            miceTiming = itemView.findViewById(R.id.miceTiming);
            receiverName = itemView.findViewById(R.id.receiverName);

        }
    }


    public static class senderViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton downlaodDoc;
        ProgressBar progressBarDoc;
        TextView downloadPercentageDocSender;
        ImageButton pauseButtonDocSender;
        RelativeLayout docDownloadControls;

        TextView sendMessage, sendTime, docName, extension, cName, cPhone, firstText, readMore;
        AppCompatImageView senderImg;
        ProgressBar progressBar;

        LinearLayout docLyt, contactContainer, viewContact;

        LinearLayout miceContainer, delete;
        ImageView miceUImage;
        AppCompatImageButton micePlay;
        LinearProgressIndicator miceProgressbar;
        TextView miceTiming;
        ImageView senderVideo;
        ConstraintLayout menu2;

        FloatingActionButton downlaod;
        RelativeLayout sendervideoLyt;
        FloatingActionButton downlaodVideo;
        ProgressBar progressBarVideo;

        View blurVideo;
        TextView captionText;

        RelativeLayout richLinkViewLyt;

        ImageView linkImg, linkImg2;
        TextView linkTitle;
        TextView linkDesc;
        TextView link, linkActualUrl;
        LinearProgressIndicator viewnew;
        LinearLayout datelyt;
        TextView dateTxt;
        LinearLayout MainSenderBox, richBox;

        ImageView pdfPreview, videoicon;
        LinearLayout docFileIcon;
        CardView pdfcard;
        TextView docSizeExtension;
        TextView docSize;
        public TextView downloadPercentageImageSender;
        public ImageButton pauseButtonImageSender;
        View blur;
        ProgressBar progressBarImageview;
        public TextView downloadPercentageVideoSender;
        public ImageButton pauseButtonVideoSender;

        // Audio download controls (voice)
        RelativeLayout audioDownloadControls;
        FloatingActionButton downlaodAudio;
        ProgressBar progressBarAudio;
        TextView downloadPercentageAudioSender;
        ImageButton pauseButtonAudioSender;


        RelativeLayout senderImgBunchLyt;
        ShapeableImageView img1, img3, img2, img4;
        FrameLayout img4Lyt;
        TextView overlayTextImg;
        FloatingActionButton downlaodImgBunch;
        TextView downloadPercentageImageSenderBunch;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            blur = itemView.findViewById(R.id.blur);
            sendMessage = itemView.findViewById(R.id.sendMessage);
            sendTime = itemView.findViewById(R.id.sendTime);
            docName = itemView.findViewById(R.id.docName);
            senderImg = itemView.findViewById(R.id.senderImg);
            docLyt = itemView.findViewById(R.id.docLyt);
            extension = itemView.findViewById(R.id.extension);
            contactContainer = itemView.findViewById(R.id.contactContainer);
            cName = itemView.findViewById(R.id.cName);
            cPhone = itemView.findViewById(R.id.cPhone);
            viewContact = itemView.findViewById(R.id.viewContact);
            firstText = itemView.findViewById(R.id.firstText);
            miceContainer = itemView.findViewById(R.id.miceContainer);
            miceUImage = itemView.findViewById(R.id.miceUImage);
            micePlay = itemView.findViewById(R.id.micePlay);
            miceProgressbar = itemView.findViewById(R.id.miceProgressbar);
            miceTiming = itemView.findViewById(R.id.miceTiming);
            viewnew = itemView.findViewById(R.id.viewnew);
            menu2 = itemView.findViewById(R.id.menu2);
            delete = itemView.findViewById(R.id.delete);
            senderVideo = itemView.findViewById(R.id.senderVideo);
            sendervideoLyt = itemView.findViewById(R.id.sendervideoLyt);
            downlaodVideo = itemView.findViewById(R.id.downlaodVideo);
            progressBarVideo = itemView.findViewById(R.id.progressBarVideo);
            blurVideo = itemView.findViewById(R.id.blurVideo);
            captionText = itemView.findViewById(R.id.captionText);
            readMore = itemView.findViewById(R.id.readMore);
            downlaod = itemView.findViewById(R.id.downlaod);
            downloadPercentageImageSender = itemView.findViewById(R.id.downloadPercentageImageSender);
            pauseButtonImageSender = itemView.findViewById(R.id.pauseButtonImageSender);
            richLinkViewLyt = itemView.findViewById(R.id.richLinkViewLyt);
            linkImg = itemView.findViewById(R.id.linkImg);
            linkImg2 = itemView.findViewById(R.id.linkImg2);
            linkTitle = itemView.findViewById(R.id.linkTitle);
            linkDesc = itemView.findViewById(R.id.linkDesc);
            link = itemView.findViewById(R.id.link);
            linkActualUrl = itemView.findViewById(R.id.linkActualUrl);

            datelyt = itemView.findViewById(R.id.datelyt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            richBox = itemView.findViewById(R.id.richBox);
            MainSenderBox = itemView.findViewById(R.id.MainSenderBox);

            pdfPreview = itemView.findViewById(R.id.pdfPreview);
            pdfcard = itemView.findViewById(R.id.pdfcard);
            docFileIcon = itemView.findViewById(R.id.docFileIcon);
            docSizeExtension = itemView.findViewById(R.id.docSizeExtension);
            docSize = itemView.findViewById(R.id.docSize);
            videoicon = itemView.findViewById(R.id.videoicon);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBarImageview = itemView.findViewById(R.id.progressBar);
            downloadPercentageVideoSender = itemView.findViewById(R.id.downloadPercentageVideoSender);
            pauseButtonVideoSender = itemView.findViewById(R.id.pauseButtonVideoSender);

            downlaodDoc = itemView.findViewById(R.id.downlaodDoc);
            progressBarDoc = itemView.findViewById(R.id.progressBarDoc);
            downloadPercentageDocSender = itemView.findViewById(R.id.downloadPercentageDocSender);
            pauseButtonDocSender = itemView.findViewById(R.id.pauseButtonDocSender);
            docDownloadControls = itemView.findViewById(R.id.docDownloadControls);

            audioDownloadControls = itemView.findViewById(R.id.audioDownloadControls);
            downlaodAudio = itemView.findViewById(R.id.downlaodAudio);
            progressBarAudio = itemView.findViewById(R.id.progressBarAudio);
            downloadPercentageAudioSender = itemView.findViewById(R.id.downloadPercentageAudioSender);
            pauseButtonAudioSender = itemView.findViewById(R.id.pauseButtonAudioSender);

            senderImgBunchLyt = itemView.findViewById(R.id.senderImgBunchLyt);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            img4 = itemView.findViewById(R.id.img4);
            img4Lyt = itemView.findViewById(R.id.img4Lyt);
            overlayTextImg = itemView.findViewById(R.id.overlayTextImg);
            downlaodImgBunch = itemView.findViewById(R.id.downlaodImgBunch);
            downloadPercentageImageSenderBunch = itemView.findViewById(R.id.downloadPercentageImageSenderBunch);

            // Ensure bunch container is clickable like normal message item
            try {
                if (senderImgBunchLyt != null) {
                    senderImgBunchLyt.setClickable(true);
                    senderImgBunchLyt.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "senderImgBunchLyt onClick -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    senderImgBunchLyt.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "senderImgBunchLyt onLongClick -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                // Forward clicks from child images/overlay to the itemView as well
                if (img1 != null) {
                    img1.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img1 clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    img1.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img1 long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                if (img2 != null) {
                    img2.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img2 clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    img2.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img2 long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                if (img3 != null) {
                    img3.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img3 clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    img3.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img3 long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                if (img4 != null) {
                    img4.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img4 clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    img4.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img4 long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                if (img4Lyt != null) {
                    img4Lyt.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img4Lyt clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    img4Lyt.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "img4Lyt long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
                if (overlayTextImg != null) {
                    overlayTextImg.setOnClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "+N overlay clicked -> forwarding to itemView.performClick()");
                        itemView.performClick();
                    });
                    overlayTextImg.setOnLongClickListener(v -> {
                        android.util.Log.d("GroupBunchClick", "+N overlay long-clicked -> forwarding to itemView.performLongClick()");
                        return itemView.performLongClick();
                    });
                }
            } catch (Exception ignored) {}
        }
    }

    public void clear() {
        notifyDataSetChanged();
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }


    public interface MediaPlayerCallback {
        void onStopMediaPlayer();
    }

    public void setMediaPlayerCallback(MediaPlayerCallback callback) {
        mediaPlayerCallback = callback;
    }

    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public class DownloadReceiver extends BroadcastReceiver {

        //this receiver for all type of files
        ProgressBar progressBarImageview;
        View blur;
        FloatingActionButton downlaod;

        public DownloadReceiver(ProgressBar progressBarImageview, View blur, FloatingActionButton downlaod) {
            this.progressBarImageview = progressBarImageview;
            this.blur = blur;
            this.downlaod = downlaod;
        }

        public DownloadReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                if (id == downloadId) {
                    // progressBarImageview.setVisibility(View.GONE);
                    //blur.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


    private void loadPdfPreview(String localPreviewImagePath, String remotePdfUrl, ImageView imageView, ViewGroup parentLayout, int position, group_messageModel model) {
        File localPreviewFile = new File(localPreviewImagePath);

        if (localPreviewFile.exists()) {
            Log.d("TAG", "Loading PDF preview from local cache: " + localPreviewImagePath);

            // Use file:// prefix for local files
            String imageSource = "file://" + localPreviewImagePath;


            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop();


            // Load low-quality preview from cache
            Constant.loadImageIntoViewGroupOptimizedPdfGroup(
                    mContext,
                    imageSource,
                    requestOptions,
                    imageView,
                    parentLayout,
                    position,
                    true, // 👈 Load low-quality
                    model
            );
        } else {
            Log.d("TAG", "Local PDF preview not found. Downloading PDF from: " + remotePdfUrl);

            // Fallback to generate and download preview
            new DownloadAndGeneratePdfPreviewTask(
                    mContext,
                    localPreviewImagePath,
                    imageView,
                    parentLayout,
                    position, true
            ).execute(remotePdfUrl);
        }
    }


    private static class DownloadAndGeneratePdfPreviewTask extends AsyncTask<String, Void, Bitmap> {
        private final Context context;
        private final String localPreviewImagePath;
        private final ImageView imageView;
        private boolean downloadSuccess = false; // Flag to track if PDF download was successful


        public DownloadAndGeneratePdfPreviewTask(Context mContext, String localPreviewImagePath, ImageView imageView, ViewGroup parentLayout, int position, boolean loadHighQuality) {
            this.context = mContext;
            this.localPreviewImagePath = localPreviewImagePath;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show a temporary placeholder image while the PDF is being downloaded and processed.
            // Replace R.drawable.inviteimg with your actual placeholder drawable.
            imageView.setImageResource(R.drawable.invite_dark);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String pdfUrl = urls[0]; // The remote URL of the PDF
            File tempPdfFile = null; // Temporary file to store the downloaded PDF
            Bitmap previewBitmap = null; // The bitmap generated from the PDF

            try {
                // --- Step 1: Download the PDF file to a temporary location ---
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000); // 10 seconds timeout for connection
                connection.setReadTimeout(15000);    // 15 seconds timeout for reading
                connection.connect();

                // Check if the HTTP connection was successful (HTTP 200 OK)
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    // Create a temporary file in the app's cache directory to store the downloaded PDF
                    tempPdfFile = File.createTempFile("temp_pdf", ".pdf", context.getCacheDir());
                    FileOutputStream output = new FileOutputStream(tempPdfFile);

                    byte[] buffer = new byte[4096]; // Buffer for reading data
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead); // Write data to the temporary file
                    }
                    output.flush(); // Ensure all buffered data is written
                    output.close();
                    input.close();
                    downloadSuccess = true; // Mark download as successful
                    Log.d("TAG", "PDF downloaded to temporary file: " + tempPdfFile.getAbsolutePath());
                } else {
                    Log.e("TAG", "Failed to download PDF. HTTP error code: " + connection.getResponseCode() + " for URL: " + pdfUrl);
                    return null; // Return null if download fails
                }

                // --- Step 2: Generate PDF preview from the downloaded temporary file ---
                if (downloadSuccess && tempPdfFile != null) {
                    ParcelFileDescriptor fileDescriptor = null;
                    PdfRenderer renderer = null;
                    try {
                        // Open the temporary PDF file for reading
                        fileDescriptor = ParcelFileDescriptor.open(tempPdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                        renderer = new PdfRenderer(fileDescriptor);
                        PdfRenderer.Page page = renderer.openPage(0); // Open the first page of the PDF

                        // Create a mutable bitmap with the same dimensions as the PDF page
                        previewBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                        // Render the PDF page onto the bitmap
                        page.render(previewBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                        page.close(); // Close the PDF page
                    } finally {
                        // Ensure PdfRenderer and ParcelFileDescriptor are closed to prevent resource leaks
                        if (renderer != null) renderer.close();
                        if (fileDescriptor != null) fileDescriptor.close();
                    }

                    // --- Step 3: Save the generated bitmap preview to the designated local path ---
                    if (previewBitmap != null) {
                        File previewFile = new File(localPreviewImagePath);
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(previewFile);
                            // Compress the bitmap to PNG format (100% quality)
                            previewBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                            Log.d("TAG", "PDF preview saved to: " + localPreviewImagePath);
                        } catch (Exception e) {
                            Log.e("TAG", "Error saving PDF preview bitmap to " + localPreviewImagePath + ": " + e.getMessage());
                        } finally {
                            if (out != null) {
                                out.close(); // Close the FileOutputStream
                            }
                        }
                    }
                }

            } catch (IOException e) {
                Log.e("TAG", "Network or file I/O error during PDF download/preview generation: " + e.getMessage());
            } catch (Exception e) {
                Log.e("TAG", "General error during PDF preview generation: " + e.getMessage());
            } finally {
                // --- Cleanup: Ensure the temporary PDF file is deleted ---
                if (tempPdfFile != null && tempPdfFile.exists()) {
                    if (tempPdfFile.delete()) {
                        Log.d("TAG", "Temporary PDF file deleted: " + tempPdfFile.getAbsolutePath());
                    } else {
                        Log.w("TAG", "Failed to delete temporary PDF file: " + tempPdfFile.getAbsolutePath());
                    }
                }
            }
            return previewBitmap; // Return the generated bitmap (or null if an error occurred)
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // This method runs on the UI thread after doInBackground completes.
            if (bitmap != null) {
                // If a bitmap was successfully generated, display it
                if (bitmap != null && localPreviewImagePath != null) {
                    group_messageModel model = new group_messageModel();
                    model.setImageWidth(String.valueOf(bitmap.getWidth()));
                    model.setImageHeight(String.valueOf(bitmap.getHeight()));
                    model.setAspectRatio(String.valueOf((float) bitmap.getWidth() / bitmap.getHeight()));
                    model.setFileName(new File(localPreviewImagePath).getName());

                    String imageSource = localPreviewImagePath.startsWith("/")
                            ? "file://" + localPreviewImagePath
                            : localPreviewImagePath;

                    ViewGroup parentLayout = (ViewGroup) imageView.getParent();

                    RequestOptions requestOptions = new RequestOptions();


                    Constant.loadImageIntoViewGroupOptimizedPdfGroup(
                            context,
                            imageSource,
                            requestOptions,
                            imageView,
                            parentLayout,
                            0, // Use real adapter position if available
                            true, // true
                            model
                    );
                } else {
                    imageView.setImageResource(R.drawable.invite_dark);
                    Log.e("TAG", "Failed to load PDF preview. Displaying placeholder.");
                }

            } else {
                // If bitmap is null, it means there was an error downloading or generating the preview.
                // Display the error placeholder image.
                imageView.setImageResource(R.drawable.invite_dark);
                Log.e("TAG", "Failed to load PDF preview. Displaying placeholder.");
            }
        }
    }

    private String getFilePath(group_messageModel model) {
        File customFolder;
        String exactPath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        } else {
            customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents");
            exactPath = customFolder.getAbsolutePath();
        }
        String filePath = exactPath + "/" + model.getFileName();
        return doesFileExist(filePath) ? filePath : model.getDocument(); // Fallback to remote URL if local file doesn't exist
    }

    private String getLocalPdfPreviewImagePath(group_messageModel model) {
        File customFolder;
        String exactPath;

        // Use getExternalFilesDir() for app-private storage.
        // Environment.DIRECTORY_DOCUMENTS is a standard directory within app-private storage.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents/Previews");
        } else {
            // For older Android versions, use the root of getExternalFilesDir()
            customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Documents/Previews");
        }

        // Ensure the directory exists. If it doesn't, create it.
        if (!customFolder.exists()) {
            boolean created = customFolder.mkdirs(); // Creates the directory and any necessary parent directories
            if (!created) {
                Log.e("TAG", "Failed to create directory: " + customFolder.getAbsolutePath());
                // Handle this error appropriately, perhaps by falling back to no preview
            }
        }

        exactPath = customFolder.getAbsolutePath();
        // Construct the full path for the preview image, using the original file name + ".png"
        return exactPath + "/" + model.getFileName() + ".png";
    }

    private String getRemotePdfUrl(group_messageModel model) {
        // Assuming model.getDocument() returns the remote URL of the PDF
        return model.getDocument();
    }


    public void updateMessageList(List<group_messageModel> newMessageList) {
        // Calculate the diff between the old and new list
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MessageDiffCallback(this.groupMessageList, newMessageList));

        // Clear the old list and add all new items
        this.groupMessageList.clear();
        this.groupMessageList.addAll(newMessageList);

        // Dispatch the updates to the adapter
        diffResult.dispatchUpdatesTo(this);
    }

    public static class MessageDiffCallback extends DiffUtil.Callback {

        private final List<group_messageModel> mOldList;
        private final List<group_messageModel> mNewList;

        public MessageDiffCallback(List<group_messageModel> oldList, List<group_messageModel> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        // Called to check whether two objects represent the same item.
        // For messages, a unique ID (like modelId) is usually the best way to determine this.
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getModelId().equals(mNewList.get(newItemPosition).getModelId());
        }

        // Called to check whether the contents of two items are the same.
        // You've already overridden equals() in your messageModel, which is perfect for this.
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
        }

        // Optional: If you want to provide payload for partial updates (e.g., only emoji count changed)
        // @Nullable
        // @Override
        // public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //     // Implement this if you want to do partial updates
        //     // For example, if only the emoji count changes, you can return a bundle
        //     // indicating that, and onBindViewHolder will receive this payload.
        //     return super.getChangePayload(oldItemPosition, newItemPosition);
        // }
    }


    private void startSenderImageDownloadWithProgress(RecyclerView.ViewHolder holder, group_messageModel model) {
        Log.d("DOWNLOAD_DEBUG", "startSenderImageDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // --- PRIVATE PATH ---
        File privateDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
        if (!privateDir.exists()) privateDir.mkdirs();
        File privateFile = new File(privateDir, model.getFileName());

        // --- Skip if already downloaded privately ---
        if (privateFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Sender image already exists privately");
            ((senderViewHolder) holder).downlaod.setVisibility(View.GONE);
            ((senderViewHolder) holder).progressBar.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageImageSender.setText("Downloaded");
            ((senderViewHolder) holder).downloadPercentageImageSender.setVisibility(View.VISIBLE);
            return;
        }

        // --- UI setup ---
        ((senderViewHolder) holder).progressBar.setVisibility(View.GONE);
        ((senderViewHolder) holder).downlaod.setVisibility(View.GONE);
        ((senderViewHolder) holder).downloadPercentageImageSender.setVisibility(View.VISIBLE);

        // --- Setup DownloadManager ---
        Log.d("DOWNLOAD_DEBUG", "Setting up DownloadManager...");
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getDocument()));

        request.setTitle(model.getFileName());
        request.setDescription("Downloading Image");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(privateFile));

        // --- Enqueue Download ---
        long downloadId = downloadManager.enqueue(request);
        Log.d("DOWNLOAD_DEBUG", "Sender Image Download ID: " + downloadId);

        // --- Track Progress ---
        trackSenderImageDownloadProgress(downloadId, ((senderViewHolder) holder).downloadPercentageImageSender, holder);

        // --- Register Receiver for completion ---
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;

                ((senderViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((senderViewHolder) holder).downloadPercentageImageSender.setText("");

                try {
                    // --- PUBLIC PATH: save in Enclosure album ---
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.DISPLAY_NAME, model.getFileName());
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");

                        Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        if (uri != null) {
                            try (OutputStream out = mContext.getContentResolver().openOutputStream(uri);
                                 InputStream in = new FileInputStream(privateFile)) {
                                byte[] buffer = new byte[8192];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                out.flush();
                            }
                            Log.d("DOWNLOAD_DEBUG", "Saved to public Enclosure album: " + model.getFileName());
                        }
                    } else {
                        // For Android < Q, copy physically and scan
                        File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Enclosure");
                        if (!publicDir.exists()) publicDir.mkdirs();
                        File publicFile = new File(publicDir, model.getFileName());
                        copyFile2(privateFile, publicFile);

                        MediaScannerConnection.scanFile(
                                mContext,
                                new String[]{publicFile.getAbsolutePath()},
                                null,
                                (path, uri) -> Log.d("DOWNLOAD_DEBUG", "File scanned: " + uri)
                        );
                        Log.d("DOWNLOAD_DEBUG", "Copied file to public Enclosure folder: " + publicFile.getAbsolutePath());
                    }

                } catch (IOException e) {
                    Log.e("DOWNLOAD_DEBUG", "Error saving to public folder", e);
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), mContext.RECEIVER_EXPORTED);
    }



    private void copyFile2(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    private void trackSenderImageDownloadProgress(long downloadId, TextView percentageView, RecyclerView.ViewHolder holder) {
        Handler handler = new Handler();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);

                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (bytesDownloadedIndex != -1 && bytesTotalIndex != -1 && statusIndex != -1) {
                        int bytesDownloaded = cursor.getInt(bytesDownloadedIndex);
                        int bytesTotal = cursor.getInt(bytesTotalIndex);

                        if (bytesTotal > 0) {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            percentageView.setText(progress + "%");

                            if (progress >= 100) {
                                percentageView.setVisibility(View.GONE);
                                ((senderViewHolder) holder).progressBar.setVisibility(View.GONE);
                                cursor.close();
                                percentageView.setText(0 + "%");
                                return;
                            }
                        }

                        int status = cursor.getInt(statusIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            percentageView.setVisibility(View.GONE);
                            ((senderViewHolder) holder).progressBar.setVisibility(View.GONE);
                            cursor.close();
                            return;
                        }
                    }
                }
                cursor.close();
                handler.postDelayed(this, 100);
            }
        };
        handler.post(progressRunnable);
    }


    private void startSenderVideoDownloadWithProgress(RecyclerView.ViewHolder holder, group_messageModel model) {
        Log.d("DOWNLOAD_DEBUG", "startSenderVideoDownloadWithProgress called");

        // --- PRIVATE VIDEO PATH ---
        File privateVideoDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Videos");
        if (!privateVideoDir.exists()) privateVideoDir.mkdirs();
        File privateVideoFile = new File(privateVideoDir, model.getFileName());

        // --- Skip if already downloaded privately ---
        if (privateVideoFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Video file already exists privately: " + privateVideoFile.getAbsolutePath());
            ((senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
            ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageVideoSender.setText("");
            ((senderViewHolder) holder).downloadPercentageVideoSender.setVisibility(View.VISIBLE);
            copyVideoToPublic(privateVideoFile, model.getFileName()); // try public copy if missing
            return;
        }

        // --- Show progress UI ---
        ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
        ((senderViewHolder) holder).downlaodVideo.setVisibility(View.GONE);
        ((senderViewHolder) holder).downloadPercentageVideoSender.setVisibility(View.VISIBLE);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getDocument()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(model.getFileName());
        request.setDescription("Downloading video...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(privateVideoFile));

        long downloadId = downloadManager.enqueue(request);
        Log.d("DOWNLOAD_DEBUG", "Sender Video Download ID: " + downloadId);

        trackSenderVideoDownloadProgress(downloadId, ((senderViewHolder) holder).downloadPercentageVideoSender, holder);

        // --- Use BroadcastReceiver to copy after DownloadManager completes ---
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != downloadId) return;

                // Hide progress UI
                ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                ((senderViewHolder) holder).downloadPercentageVideoSender.setText("");

                // --- Copy video to public folder ---
                copyVideoToPublic(privateVideoFile, model.getFileName());
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), mContext.RECEIVER_EXPORTED);

        // --- Download thumbnail privately only ---
        if (model.getThumbnail() != null && !model.getThumbnail().isEmpty()) {
            File thumbnailDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Enclosure/Media/Thumbnail");
            if (!thumbnailDir.exists()) thumbnailDir.mkdirs();

            String thumbnailFileName = model.getFileNameThumbnail();
            File thumbnailFile = new File(thumbnailDir, thumbnailFileName);

            DownloadManager.Request thumbRequest = new DownloadManager.Request(Uri.parse(model.getThumbnail()));
            thumbRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            thumbRequest.setAllowedOverRoaming(false);
            thumbRequest.setTitle("Downloading thumbnail");
            thumbRequest.setDescription("Thumbnail download in progress...");
            thumbRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            thumbRequest.setDestinationUri(Uri.fromFile(thumbnailFile));

            downloadManager.enqueue(thumbRequest);
        }
    }

    private void copyVideoToPublic(File privateFile, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Enclosure");
                values.put(MediaStore.Video.Media.IS_PENDING, 1);

                Uri uri = mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(privateFile);
                         OutputStream out = mContext.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        out.flush();
                    }
                    values.clear();
                    values.put(MediaStore.Video.Media.IS_PENDING, 0);
                    mContext.getContentResolver().update(uri, values, null, null);
                    Log.d("DOWNLOAD_DEBUG", "Video saved publicly via MediaStore: " + fileName);
                }
            } else {
                File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Enclosure");
                if (!publicDir.exists()) publicDir.mkdirs();
                File publicFile = new File(publicDir, fileName);

                copyFile2(privateFile, publicFile);

                MediaScannerConnection.scanFile(
                        mContext,
                        new String[]{publicFile.getAbsolutePath()},
                        null,
                        (path, uri) -> Log.d("DOWNLOAD_DEBUG", "Video scanned: " + uri)
                );
                Log.d("DOWNLOAD_DEBUG", "Video copied publicly: " + publicFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("DOWNLOAD_DEBUG", "Error copying video to public folder", e);
        }
    }


    private void trackSenderVideoDownloadProgress(long downloadId, TextView percentageView, RecyclerView.ViewHolder holder) {
        Handler handler = new Handler();
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);

                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

                    if (bytesDownloadedIndex != -1 && bytesTotalIndex != -1 && statusIndex != -1) {
                        long bytesDownloaded = cursor.getLong(bytesDownloadedIndex);
                        long bytesTotal = cursor.getLong(bytesTotalIndex);

                        if (bytesTotal > 0) {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            percentageView.setText(progress + "%");
                        }

                        int status = cursor.getInt(statusIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            percentageView.setVisibility(View.GONE);
                            ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
                            ((senderViewHolder) holder).blurVideo.setVisibility(View.GONE);
                            cursor.close();
                            return;
                        }
                    }
                }
                cursor.close();
                handler.postDelayed(this, 100);
            }
        };
        handler.post(progressRunnable);
    }


    private void startSenderDocDownloadWithProgress(RecyclerView.ViewHolder holder, group_messageModel model) {
        Log.d("DOWNLOAD_DEBUG", "startSenderDocDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((senderViewHolder) holder).downlaodDoc.setVisibility(View.GONE);
        ((senderViewHolder) holder).progressBarVideo.setVisibility(View.GONE);
        ((senderViewHolder) holder).downloadPercentageDocSender.setVisibility(View.VISIBLE);
        ((senderViewHolder) holder).downloadPercentageDocSender.setText("0%");

        // --- PRIVATE DOCUMENT DIRECTORY ---
        File privateDocsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!privateDocsDir.exists()) privateDocsDir.mkdirs();
        File privateDocFile = new File(privateDocsDir, model.getFileName());

        // --- If already exists ---
        if (privateDocFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Document already exists, skipping download");
            ((senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageDocSender.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageDocSender.setText("");

            copyDocToPublicDoc(privateDocFile, model.getFileName()); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading document");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateDocFile));

        long docDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Document Download ID: " + docDownloadId);

        // --- Track progress ---
        trackSenderDocDownloadProgress(
                docDownloadId,
                ((senderViewHolder) holder).progressBarDoc,
                ((senderViewHolder) holder).downloadPercentageDocSender,
                ((senderViewHolder) holder).downlaodDoc
        );

        // --- Copy to public after download completes ---
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != docDownloadId) return;

                ((senderViewHolder) holder).progressBarDoc.setVisibility(View.GONE);
                ((senderViewHolder) holder).downloadPercentageDocSender.setText("");

                // Copy document to public folder
                copyDocToPublicDoc(privateDocFile, model.getFileName());
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), mContext.RECEIVER_EXPORTED);
    }




    // --- Copy private document to public Documents/Enclosure ---
    private void copyDocToPublicDoc(File privateFile, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/octet-stream");
                values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Enclosure");
                values.put(MediaStore.Files.FileColumns.IS_PENDING, 1);

                Uri uri = mContext.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                if (uri != null) {
                    try (InputStream in = new FileInputStream(privateFile);
                         OutputStream out = mContext.getContentResolver().openOutputStream(uri)) {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                        out.flush();
                    }
                    values.clear();
                    values.put(MediaStore.Files.FileColumns.IS_PENDING, 0);
                    mContext.getContentResolver().update(uri, values, null, null);
                    Log.d("DOWNLOAD_DEBUG", "Document saved publicly via MediaStore: " + fileName);
                }
            } else {
                File publicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Enclosure");
                if (!publicDir.exists()) publicDir.mkdirs();
                File publicFile = new File(publicDir, fileName);

                copyFile2(privateFile, publicFile);

                MediaScannerConnection.scanFile(
                        mContext,
                        new String[]{publicFile.getAbsolutePath()},
                        null,
                        (path, uri) -> Log.d("DOWNLOAD_DEBUG", "Document scanned: " + uri)
                );
                Log.d("DOWNLOAD_DEBUG", "Document copied publicly: " + publicFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.e("DOWNLOAD_DEBUG", "Error copying document to public folder", e);
        }
    }

    private void trackSenderDocDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor c = dm.query(q);
                    if (c != null && c.moveToFirst()) {
                        int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int bytesIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                        int status = statusIdx >= 0 ? c.getInt(statusIdx) : -1;
                        long soFar = (bytesIdx >= 0) ? c.getLong(bytesIdx) : 0L;
                        long total = (totalIdx >= 0) ? c.getLong(totalIdx) : 0L;

                        if (total > 0) {
                            progressBar.setIndeterminate(false);
                            progressBar.setMax(100);
                            int prog = (int) ((soFar * 100L) / total);
                            progressBar.setProgress(prog);
                            percentageView.setText(prog + "%");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            downloadFab.setVisibility(View.GONE);
                            c.close();
                            return;
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            downloadFab.setVisibility(View.VISIBLE);
                            c.close();
                            return;
                        }
                        c.close();
                        handler.postDelayed(this, 300);
                    } else {
                        if (c != null) c.close();
                        handler.postDelayed(this, 300);
                    }
                } catch (Exception ignored) {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                    downloadFab.setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(r);
    }

    private void startSenderAudioDownloadWithProgress(RecyclerView.ViewHolder holder, group_messageModel model) {
        Log.d("DOWNLOAD_DEBUG", "startSenderDocDownloadWithProgress called");
        Log.d("DOWNLOAD_DEBUG", "Document URL: " + model.getDocument());
        Log.d("DOWNLOAD_DEBUG", "File Name: " + model.getFileName());

        // Prepare UI
        ((senderViewHolder) holder).downlaodAudio.setVisibility(View.GONE);
        ((senderViewHolder) holder).progressBarAudio.setIndeterminate(true);
        ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
        ((senderViewHolder) holder).downloadPercentageAudioSender.setText("0%");
        ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.VISIBLE);

        // --- PRIVATE DOCUMENT DIRECTORY ---
        File privateDocsDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Audios");
        if (!privateDocsDir.exists()) privateDocsDir.mkdirs();
        File privateDocFile = new File(privateDocsDir, model.getFileName());

        // --- If already exists ---
        if (privateDocFile.exists()) {
            Log.d("DOWNLOAD_DEBUG", "Document already exists, skipping download");
            ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
            ((senderViewHolder) holder).downloadPercentageAudioSender.setText("");

            copyDocToPublicDoc(privateDocFile, model.getFileName()); // Copy to public if missing
            return;
        }

        // --- Setup DownloadManager ---
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(model.getDocument()));
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        req.setAllowedOverRoaming(false);
        req.setTitle(model.getFileName());
        req.setDescription("Downloading document");
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        req.setDestinationUri(Uri.fromFile(privateDocFile));

        long docDownloadId = dm.enqueue(req);
        Log.d("DOWNLOAD_DEBUG", "Document Download ID: " + docDownloadId);

        // --- Track progress ---
        trackSenderDocDownloadProgress(
                docDownloadId,
                ((senderViewHolder) holder).progressBarAudio,
                ((senderViewHolder) holder).downloadPercentageAudioSender,
                ((senderViewHolder) holder).downlaodAudio
        );

        // --- Copy to public after download completes ---
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id != docDownloadId) return;

                ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
                ((senderViewHolder) holder).downloadPercentageAudioSender.setText("");

                // Copy document to public folder
                copyDocToPublicDoc(privateDocFile, model.getFileName());
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), mContext.RECEIVER_EXPORTED);
    }
    private void startSenderAudioDownloadWithProgressXDocument(RecyclerView.ViewHolder holder, group_messageModel model) {
        // Prepare UI
        ((senderViewHolder) holder).downlaodAudio.setVisibility(View.GONE);
        ((senderViewHolder) holder).progressBarAudio.setIndeterminate(true);
        ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
        ((senderViewHolder) holder).downloadPercentageAudioSender.setText("0%");
        ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.VISIBLE);

        // Ensure destination dir exists
        File audiosDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Documents");
        if (!audiosDir.exists()) audiosDir.mkdirs();

        try {
            DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(model.getDocument());
            DownloadManager.Request req = new DownloadManager.Request(uri)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setTitle(model.getFileName())
                    .setDescription("Downloading audio")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalFilesDir(
                            mContext,
                            Environment.DIRECTORY_DOCUMENTS,
                            "Enclosure/Media/Documents/" + model.getFileName()
                    );

            long audioDownloadId = dm.enqueue(req);
            trackSenderAudioDownloadProgress(
                    audioDownloadId,
                    ((senderViewHolder) holder).progressBarAudio,
                    ((senderViewHolder) holder).downloadPercentageAudioSender,
                    ((senderViewHolder) holder).downlaodAudio
            );
        } catch (Exception e) {
            // Reset UI on error
            ((senderViewHolder) holder).downloadPercentageAudioSender.setVisibility(View.GONE);
            ((senderViewHolder) holder).progressBarAudio.setVisibility(View.GONE);
            ((senderViewHolder) holder).downlaodAudio.setVisibility(View.VISIBLE);
        }
    }

    private void trackSenderAudioDownloadProgress(long downloadId, ProgressBar progressBar, TextView percentageView, View downloadFab) {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query q = new DownloadManager.Query().setFilterById(downloadId);
                    Cursor c = dm.query(q);
                    if (c != null && c.moveToFirst()) {
                        int statusIdx = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int bytesIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int totalIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                        int status = c.getInt(statusIdx);
                        long bytesDownloaded = c.getLong(bytesIdx);
                        long totalBytes = c.getLong(totalIdx);

                        if (totalBytes > 0) {
                            int pct = (int) ((bytesDownloaded * 100L) / totalBytes);
                            percentageView.setText(pct + "%");
                        } else {
                            percentageView.setText("0%");
                        }

                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            progressBar.setVisibility(View.GONE);
                            percentageView.setVisibility(View.GONE);
                            downloadFab.setVisibility(View.GONE); // file exists now, so keep hidden
                            c.close();
                            return;
                        }
                        c.close();
                        handler.postDelayed(this, 300);
                    } else {
                        if (c != null) c.close();
                        handler.postDelayed(this, 300);
                    }
                } catch (Exception ignored) {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                    downloadFab.setVisibility(View.VISIBLE);
                }
            }
        };
        handler.post(r);
    }

    private void setImageViewDimensions(ImageView imageView, String widthStr, String heightStr) {
        try {
            // Set consistent scale type for all images
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (widthStr != null && heightStr != null && !widthStr.isEmpty() && !heightStr.isEmpty()) {
                // Get device density and orientation
                float density = mContext.getResources().getDisplayMetrics().density;
                int orientation = mContext.getResources().getConfiguration().orientation;

                Log.d("setImageViewDimensions", "getImageWidth: " + widthStr);
                Log.d("setImageViewDimensions", "getImageHeight: " + heightStr);
                Log.d("setImageViewDimensions", "Orientation: " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape"));

                float imageWidthPx, imageHeightPx, aspectRatio;
                try {
                    imageWidthPx = Float.parseFloat(widthStr);
                    imageHeightPx = Float.parseFloat(heightStr);
                    aspectRatio = imageWidthPx / imageHeightPx;

                    if (aspectRatio <= 0) {
                        aspectRatio = 1.0f;
                    }
                } catch (NumberFormatException e) {
                    Log.e("setImageViewDimensions", "Invalid dimensions, using defaults", e);
                    imageWidthPx = 210f;
                    imageHeightPx = 250f;
                    aspectRatio = 1.0f;
                }

                // Use the same max dimensions as loadImageIntoViewGroup
                final float MAX_WIDTH_DP = 210f;
                final float MAX_HEIGHT_DP = 250f;

                int maxWidthPx = (int) (MAX_WIDTH_DP * density);
                int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

                int finalWidthPx, finalHeightPx;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    finalWidthPx = maxWidthPx;
                    finalHeightPx = (int) (maxWidthPx / aspectRatio);
                    if (finalHeightPx > maxHeightPx) {
                        finalHeightPx = maxHeightPx;
                        finalWidthPx = (int) (maxHeightPx * aspectRatio);
                    }
                } else {
                    finalHeightPx = maxHeightPx;
                    finalWidthPx = (int) (finalHeightPx * aspectRatio);
                    if (finalWidthPx > maxWidthPx) {
                        finalWidthPx = maxWidthPx;
                        finalHeightPx = (int) (maxWidthPx / aspectRatio);
                    }
                }

                finalWidthPx = Math.min(finalWidthPx, maxWidthPx);
                finalHeightPx = Math.min(finalHeightPx, maxHeightPx);

                // Set layout parameters
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.LayoutParams(finalWidthPx, finalHeightPx);
                } else {
                    params.width = finalWidthPx;
                    params.height = finalHeightPx;
                }
                imageView.setLayoutParams(params);
                // Also set parent layout to wrap content if available
                ViewGroup parentLayout = (ViewGroup) imageView.getParent();
                if (parentLayout != null) {
                    ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
                    if (parentParams != null) {
                        parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        parentLayout.setLayoutParams(parentParams);
                    }
                }

                Log.d("setImageViewDimensions", "Set dimensions using density-based logic - Width: " + finalWidthPx + "px, Height: " + finalHeightPx + "px (Original: " + imageWidthPx + "x" + imageHeightPx + ", Aspect Ratio: " + aspectRatio + ", Density: " + density + ")");
            }
        } catch (Exception e) {
            Log.e("setImageViewDimensions", "Error setting image dimensions: " + e.getMessage());
        }
    }


    private void bindSelectionBunchImages(senderViewHolder holder,
                                          group_messageModel model,
                                          RequestOptions requestOptions,
                                          int position,
                                          boolean loadHighQuality) {

        Log.d("SelectionBunch", "bindSelectionBunchImages called for messageId=" + model.getModelId() +
                ", selectionCount=" + model.getSelectionCount() +
                ", selectionBunch=" + (model.getSelectionBunch() != null ? "not null" : "null") +
                ", selectionBunch size=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

        if (model.getSelectionBunch() == null) {
            Log.w("SelectionBunch", "selectionBunch is null for messageId=" + model.getModelId());
            return;
        }

        if (model.getSelectionBunch().size() < 2) {
            Log.w("SelectionBunch", "selectionBunch size=" + model.getSelectionBunch().size() + " (need >=2) for messageId=" + model.getModelId());
            return;
        }

        // Check if any images in selectionBunch are missing locally
        boolean anyImagesMissing = checkAnySelectionBunchImagesMissing(model.getSelectionBunch());
        boolean hasValidImageUrls = checkIfSelectionBunchHasValidUrls(model.getSelectionBunch());
        boolean isFromLocalStorage = checkIfSelectionBunchFromLocalStorage(model.getSelectionBunch());
        Log.d("bunch###", "Download icon check - anyImagesMissing: " + anyImagesMissing + ", hasValidUrls: " + hasValidImageUrls + ", isFromLocalStorage: " + isFromLocalStorage + " for messageId: " + model.getModelId());
        Log.d("bunch###", "Download condition: anyImagesMissing(" + anyImagesMissing + ") && hasValidImageUrls(" + hasValidImageUrls + ") = " + (anyImagesMissing && hasValidImageUrls));

        // For preview (when imgUrl is empty), show download button to indicate upload in progress
        boolean isPreviewMode = !hasValidImageUrls && isFromLocalStorage;
        Log.d("bunch###", "Preview mode calculation: !hasValidImageUrls(" + !hasValidImageUrls + ") && isFromLocalStorage(" + isFromLocalStorage + ") = " + isPreviewMode);

        if (anyImagesMissing && hasValidImageUrls) {
            // Show download views when images are missing but have valid URLs (regardless of local storage status)
            holder.downlaodImgBunch.setVisibility(View.VISIBLE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.VISIBLE);
            holder.downloadPercentageImageSenderBunch.setText("0%");

            // Set click listener for download button
            holder.downlaodImgBunch.setOnClickListener(v -> {
                downloadAllSelectionBunchImages(holder, model, position);
            });

            Log.d("SelectionBunch", "Some images missing locally, showing download views for messageId=" + model.getModelId());
            Log.d("bunch###", "Showing download icon - some images missing locally");
        } else if (isPreviewMode) {
            // Show download button for preview mode (upload in progress)
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            // Disable click for preview mode
            holder.downlaodImgBunch.setOnClickListener(null);

            Log.d("SelectionBunch", "Preview mode - showing upload progress for messageId=" + model.getModelId());
            Log.d("bunch###", "Showing upload progress - preview mode");
        } else {
            // Hide download views when all images exist locally or when images don't have valid URLs
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);

            Log.d("SelectionBunch", "All images exist locally or no valid URLs, hiding download views for messageId=" + model.getModelId());
            Log.d("bunch###", "Hiding download icon - all images exist locally or no valid URLs");
        }

        // Set width to 125dp for all selectionBunch images (only if changed to avoid layout thrash)
        float widthInDp = 125f;
        int widthInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp,
                holder.img1.getResources().getDisplayMetrics()
        );

        // Set standard width for all images first
        ViewGroup.LayoutParams params1 = holder.img1.getLayoutParams();
        if (params1 != null) {
            params1.width = widthInPx;
            holder.img1.setLayoutParams(params1);
        }

        ViewGroup.LayoutParams params2 = holder.img2.getLayoutParams();
        if (params2 != null) {
            params2.width = widthInPx;
            holder.img2.setLayoutParams(params2);
        }

        ViewGroup.LayoutParams params3 = holder.img3.getLayoutParams();
        if (params3 != null) {
            params3.width = widthInPx;
            holder.img3.setLayoutParams(params3);
        }

        ViewGroup.LayoutParams params4 = holder.img4.getLayoutParams();
        if (params4 != null) {
            params4.width = widthInPx;
            holder.img4.setLayoutParams(params4);
        }

        ViewGroup img1Parent = (ViewGroup) holder.img1.getParent();
        ViewGroup img3Parent = (ViewGroup) holder.img3.getParent();
        ViewGroup img4Parent = (ViewGroup) holder.img4.getParent();

        // Load first image into img1
        loadSelectionImageIntoViewForBunch(
                mContext,
                model.getSelectionBunch().get(0),
                model,
                requestOptions,
                holder.img1,
                img1Parent,
                position,
                true,
                holder.videoicon
        );

        // Load second image into img3
        loadSelectionImageIntoViewForBunch(
                mContext,
                model.getSelectionBunch().get(1),
                model,
                requestOptions,
                holder.img3,
                img3Parent,
                position,
                true,
                holder.videoicon
        );

        // Load third image into img4 if selectionCount is "3" or "4"
        if (model.getSelectionCount().equals("3") || model.getSelectionCount().equals("4")) {
            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(2),
                    model,
                    requestOptions,
                    holder.img4,
                    img4Parent,
                    position,
                    true,
                    holder.videoicon
            );
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=3
        if (model.getSelectionCount().equals("3")) {
            float img1HeightInDp = 251.5f;
            float otherHeightInDp = 125f;

            int img1HeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    img1HeightInDp,
                    holder.img1.getResources().getDisplayMetrics()
            );

            int otherHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    otherHeightInDp,
                    holder.img1.getResources().getDisplayMetrics()
            );

            // Force set img1 dimensions
            ViewGroup.LayoutParams finalParams1 = holder.img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = img1HeightInPx;
                holder.img1.setLayoutParams(finalParams1);
                Log.d("SelectionBunch", "FORCE Set img1 to " + widthInPx + "x" + img1HeightInPx + "px");
            }

            // Force set img3 dimensions
            ViewGroup.LayoutParams finalParams3 = holder.img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = otherHeightInPx;
                holder.img3.setLayoutParams(finalParams3);
                Log.d("SelectionBunch", "FORCE Set img3 to " + widthInPx + "x" + otherHeightInPx + "px");
            }

            // Force set img4 dimensions
            ViewGroup.LayoutParams finalParams4 = holder.img4.getLayoutParams();
            if (finalParams4 != null) {
                finalParams4.width = widthInPx;
                finalParams4.height = otherHeightInPx;
                holder.img4.setLayoutParams(finalParams4);
                Log.d("SelectionBunch", "FORCE Set img4 to " + widthInPx + "x" + otherHeightInPx + "px");
            }

            // Set corner radius for img4 (bottom-right only)
            float cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    20,
                    holder.img4.getResources().getDisplayMetrics()
            );

            ShapeAppearanceModel shapeModel4 = holder.img4.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                    .build();

            holder.img4.setShapeAppearanceModel(shapeModel4);
            Log.d("SelectionBunch", "Set img4 corner radius: bottom-right 20dp");
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=2
        if (model.getSelectionCount().equals("2")) {
            float allHeightInDp = 251.5f;
            int allHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    allHeightInDp,
                    holder.img1.getResources().getDisplayMetrics()
            );

            // Force set img1 and img3 to 125dp x 125dp
            ViewGroup.LayoutParams finalParams1 = holder.img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = allHeightInPx;
                holder.img1.setLayoutParams(finalParams1);
            }

            ViewGroup.LayoutParams finalParams3 = holder.img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = allHeightInPx;
                holder.img3.setLayoutParams(finalParams3);
            }

            Log.d("SelectionBunch", "FORCE Set img1 and img3 for selectionCount=2 to " + widthInPx + "x" + allHeightInPx + "px");
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=4
        if (model.getSelectionCount().equals("4")) {
            float allHeightInDp = 125.5f;
            int allHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    allHeightInDp,
                    holder.img1.getResources().getDisplayMetrics()
            );

            // Force set all images to 125dp x 125dp
            ViewGroup.LayoutParams finalParams1 = holder.img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = allHeightInPx;
                holder.img1.setLayoutParams(finalParams1);
                Log.d("SelectionBunch", "FORCE Set img1 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            Log.d("SelectionBunch", "=== FORCE IMG2 DIMENSIONS START for selectionCount=4 ===");
            Log.d("SelectionBunch", "img2 before force - visibility: " + holder.img2.getVisibility() +
                    ", width: " + holder.img2.getWidth() +
                    ", height: " + holder.img2.getHeight());

            ViewGroup.LayoutParams finalParams2 = holder.img2.getLayoutParams();
            if (finalParams2 != null) {
                Log.d("SelectionBunch", "img2 force layoutParams before: " + finalParams2.width + "x" + finalParams2.height);
                finalParams2.width = widthInPx;
                finalParams2.height = allHeightInPx;
                holder.img2.setLayoutParams(finalParams2);

                // Force layout pass to apply the new dimensions
                holder.img2.requestLayout();
                holder.img2.invalidate();

                Log.d("SelectionBunch", "FORCE Set img2 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
                Log.d("SelectionBunch", "img2 after force - width: " + holder.img2.getWidth() +
                        ", height: " + holder.img2.getHeight());
            } else {
                Log.e("SelectionBunch", "img2 force layoutParams is NULL!");
            }

            ViewGroup.LayoutParams finalParams3 = holder.img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = allHeightInPx;
                holder.img3.setLayoutParams(finalParams3);
                Log.d("SelectionBunch", "FORCE Set img3 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            ViewGroup.LayoutParams finalParams4 = holder.img4.getLayoutParams();
            if (finalParams4 != null) {
                finalParams4.width = widthInPx;
                finalParams4.height = allHeightInPx;
                holder.img4.setLayoutParams(finalParams4);
                Log.d("SelectionBunch", "FORCE Set img4 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            Log.d("SelectionBunch", "FORCE Set all images for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");

            holder.img2.setVisibility(View.VISIBLE);


            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(3),
                    model,
                    requestOptions,
                    holder.img2,
                    img4Parent,
                    position,
                    true,
                    holder.videoicon
            );
        }

        // Load fourth image into img2 if selectionCount is "4"
        Log.d("SelectionBunch", "=== IMG2 LOADING CHECK START ===");
        Log.d("SelectionBunch", "selectionCount: " + model.getSelectionCount() +
                ", bunchSize: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

        if (model.getSelectionBunch().size() > 4) {
            Log.d("SelectionBunch", "=== LOADING IMG2 for selectionCount=4 ===");

            Log.d("SelectionBunch", "IMG2 DEBUG START");
            Log.d("SelectionBunch", "IMG2 TEST 1");
            System.out.println("IMG2 TEST 1 - System.out.println");

            ViewGroup img2Parent = (ViewGroup) holder.img2.getParent();
            Log.d("SelectionBunch", "img2 parent: " + (img2Parent != null ? img2Parent.getClass().getSimpleName() : "NULL"));

            // Check img2 state before loading
            Log.d("SelectionBunch", "img2 before loading - visibility: " + holder.img2.getVisibility() +
                    ", width: " + holder.img2.getWidth() +
                    ", height: " + holder.img2.getHeight() +
                    ", alpha: " + holder.img2.getAlpha() +
                    ", scaleX: " + holder.img2.getScaleX() +
                    ", scaleY: " + holder.img2.getScaleY());

            Log.d("SelectionBunch", "Calling loadSelectionImageIntoViewForBunch for img2...");
            Log.d("SelectionBunch", "IMG2 TEST 2");
            System.out.println("IMG2 TEST 2 - System.out.println");
            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(0),
                    model,
                    requestOptions,
                    holder.img1,
                    img2Parent,
                    position,
                    true,
                    holder.videoicon
            );



            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(1),
                    model,
                    requestOptions,
                    holder.img2,
                    img2Parent,
                    position,
                    true,
                    holder.videoicon
            );


            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(2),
                    model,
                    requestOptions,
                    holder.img3,
                    img2Parent,
                    position,
                    true,
                    holder.videoicon
            );

            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(3),
                    model,
                    requestOptions,
                    holder.img4,
                    img2Parent,
                    position,
                    true,
                    holder.videoicon
            );

            holder.overlayTextImg.setVisibility(View.VISIBLE);


            Log.d("SelectionBunch", "Completed loading img2 for selectionCount=4");
            Log.d("SelectionBunch", "IMG2 TEST 3");
            System.out.println("IMG2 TEST 3 - System.out.println");

            // Additional debugging for img2 state
            Log.d("SelectionBunch", "img2 after loading - visibility: " + holder.img2.getVisibility() +
                    ", width: " + holder.img2.getWidth() +
                    ", height: " + holder.img2.getHeight() +
                    ", alpha: " + holder.img2.getAlpha() +
                    ", scaleX: " + holder.img2.getScaleX() +
                    ", scaleY: " + holder.img2.getScaleY() +
                    ", layoutParams: " + holder.img2.getLayoutParams());
            Log.d("SelectionBunch", "img2 drawable: " + (holder.img2.getDrawable() != null ? "NOT NULL" : "NULL"));

            // Final comprehensive check
            Log.d("SelectionBunch", "IMG2 FINAL: V=" + holder.img2.getVisibility() + " W=" + holder.img2.getWidth() + " H=" + holder.img2.getHeight());
            Log.d("SelectionBunch", "IMG2 FINAL: Alpha=" + holder.img2.getAlpha() + " Scale=" + holder.img2.getScaleX() + " Drawable=" + (holder.img2.getDrawable() != null));
            Log.d("SelectionBunch", "IMG2 FINAL: Shown=" + holder.img2.isShown());
        }

        // Set visibility based on selection count
        Log.d("SelectionBunch", "=== VISIBILITY SETTING START ===");
        Log.d("SelectionBunch", "Setting visibility for selectionCount: " + model.getSelectionCount());

        if (model.getSelectionCount().equals("2")) {
            Log.d("SelectionBunch", "Setting img2 to GONE for selectionCount=2");
            holder.img2.setVisibility(View.GONE);
            holder.img4.setVisibility(View.GONE);
            holder.img4Lyt.setVisibility(View.GONE);
        } else if (model.getSelectionCount().equals("3")) {
            Log.d("SelectionBunch", "Setting img2 to GONE for selectionCount=3");
            holder.img2.setVisibility(View.GONE);
            holder.img4.setVisibility(View.VISIBLE);
            holder.img4Lyt.setVisibility(View.VISIBLE);
        } else
        if (model.getSelectionCount().equals("4")) {
            Log.d("SelectionBunch", "Setting img2 to VISIBLE for selectionCount=4");
            Log.d("SelectionBunch", "img2 before visibility set - visibility: " + holder.img2.getVisibility());
            holder.img2.setVisibility(View.VISIBLE);
            Log.d("SelectionBunch", "img2 after visibility set - visibility: " + holder.img2.getVisibility());
            holder.img4.setVisibility(View.VISIBLE);
            holder.img4Lyt.setVisibility(View.VISIBLE);
        }

        Log.d("SelectionBunch", "Final img2 state - visibility: " + holder.img2.getVisibility() +
                ", width: " + holder.img2.getWidth() +
                ", height: " + holder.img2.getHeight());

    }

    private void loadSelectionImageIntoViewForBunch(Context context,
                                                    selectionBunchModel bunch,
                                                    group_messageModel model,
                                                    RequestOptions requestOptions,
                                                    ImageView targetImageView,
                                                    ViewGroup parentLayout,
                                                    int position,
                                                    boolean loadHighQuality,
                                                    View videoIcon) {

        if (bunch == null || targetImageView == null) {
            Log.w("SelectionBunch", "loadSelectionImageIntoViewForBunch skipped (bunch or targetImageView null)");
            return;
        }

        String imageSource = null;
        String fileName = bunch.getFileName();

        if (!TextUtils.isEmpty(fileName)) {
            // Decode URL-encoded characters in filename
            String decodedFileName = fileName;
            try {
                decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
                Log.d("bunch###", "Decoded filename: " + fileName + " -> " + decodedFileName);
            } catch (Exception e) {
                Log.w("bunch###", "Failed to decode filename: " + fileName, e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            String cleanFileName = decodedFileName;
            if (cleanFileName.contains("/")) {
                cleanFileName = cleanFileName.substring(cleanFileName.lastIndexOf("/") + 1);
                Log.d("bunch###", "Removed subdirectory prefix: " + decodedFileName + " -> " + cleanFileName);
            }

            // First check cache folder for preview images
            File cacheFolder = context.getCacheDir();
            String cachePath = cacheFolder.getAbsolutePath() + "/" + cleanFileName;
            Log.d("SelectionBunch", "Checking cache path for bunch image: " + cachePath);
            Log.d("bunch###", "Checking cache path for bunch image: " + cachePath);
            Log.d("bunch###", "Cache folder exists: " + cacheFolder.exists() + ", isDirectory: " + cacheFolder.isDirectory());
            Log.d("bunch###", "Cache folder absolute path: " + cacheFolder.getAbsolutePath());
            Log.d("bunch###", "Clean filename: " + cleanFileName);
            Log.d("bunch###", "File exists check result: " + doesFileExist(cachePath));
            if (doesFileExist(cachePath)) {
                imageSource = cachePath;
                Log.d("SelectionBunch", "Found cache bunch image: " + imageSource);
                Log.d("bunch###", "✅ Found cache bunch image: " + imageSource);
            } else {
                // Check full quality cache folder
                String fullCachePath = cacheFolder.getAbsolutePath() + "/full_" + cleanFileName;
                Log.d("SelectionBunch", "Checking full cache path for bunch image: " + fullCachePath);
                Log.d("bunch###", "Checking full cache path for bunch image: " + fullCachePath);
                Log.d("bunch###", "Full cache file exists check result: " + doesFileExist(fullCachePath));
                if (doesFileExist(fullCachePath)) {
                    imageSource = fullCachePath;
                    Log.d("SelectionBunch", "Found full cache bunch image: " + imageSource);
                    Log.d("bunch###", "✅ Found full cache bunch image: " + imageSource);
                } else {
                    // Fallback to external storage folder
                    File customFolder2;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        customFolder2 = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                    } else {
                        customFolder2 = new File(context.getExternalFilesDir(null), "Enclosure/Media/Images");
                    }

                    String localPath = customFolder2.getAbsolutePath() + "/" + cleanFileName;
                    Log.d("SelectionBunch", "Checking external storage path for bunch image: " + localPath);
                    Log.d("bunch###", "Checking external storage path for bunch image: " + localPath);
                    if (doesFileExist(localPath)) {
                        imageSource = localPath;
                        Log.d("SelectionBunch", "Found external storage bunch image: " + imageSource);
                        Log.d("bunch###", "Found external storage bunch image: " + imageSource);
                    } else {
                        Log.w("bunch###", "Local bunch image not found in any location");
                    }
                }
            }
        }

        if (TextUtils.isEmpty(imageSource)) {
            if (!TextUtils.isEmpty(bunch.getImgUrl())) {
                imageSource = bunch.getImgUrl();
                Log.d("SelectionBunch", "Using remote URL for bunch image: " + imageSource);
            } else if (!TextUtils.isEmpty(model.getDocument())) {
                imageSource = model.getDocument();
                Log.d("SelectionBunch", "Falling back to message document for bunch image: " + imageSource);
            }
        }

        if (TextUtils.isEmpty(imageSource)) {
            Log.w("SelectionBunch", "No image source resolved for bunch; clearing image view");
            // Log.w("bunch###", "❌ No image source found for filename: " + fileName + ", cleanFileName: " + cleanFileName);
            Log.w("bunch###", "❌ Image view will be cleared - this causes blank preview");
            targetImageView.setImageDrawable(null);
            return;
        }

        Log.d("SelectionBunch", "Loading bunch image from: " + imageSource);

        // Use custom image loading that respects our custom dimensions
        // Both BlurImageOptimizer and Constant.loadImageIntoView() override our custom dimensions
        loadSelectionBunchImageWithCustomDimensions(context, imageSource, requestOptions, targetImageView, position, true, videoIcon);
    }

    private void loadSelectionBunchImageWithCustomDimensions(Context context, String imageSource, RequestOptions requestOptions,
                                                             ImageView targetImageView, int position, boolean loadHighQuality, View videoIcon) {

        Log.d("SelectionBunch", "loadSelectionBunchImageWithCustomDimensions called for: " + imageSource);

        // Set video icon visibility based on quality
        if (videoIcon != null) {
            videoIcon.setVisibility(true ? View.VISIBLE : View.VISIBLE);
        }

        // Use simple Glide loading that respects our custom dimensions
        Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position + "_selectionBunch"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .dontAnimate()
                        .timeout(10000))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("SelectionBunch", "Failed to load selectionBunch image: " + imageSource, e);
                        Log.e("bunch###", "Failed to load bunch image: " + imageSource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("SelectionBunch", "Successfully loaded selectionBunch image: " + imageSource);
                        Log.d("bunch###", "Successfully loaded bunch image: " + imageSource);
                        return false;
                    }
                })
                .into(targetImageView);
    }

    /**
     * Check if selection bunch has valid image URLs for group chat
     */
    private boolean checkIfSelectionBunchHasValidUrls(List<selectionBunchModel> selectionBunch) {
        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return false; // No images, so no valid URLs
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null) {
                continue; // Skip if no bunch
            }

            // Check if Firebase URL is available and not empty
            if (!TextUtils.isEmpty(bunch.getImgUrl())) {
                return true; // Found at least one valid URL
            }
        }
        return false; // No valid URLs found
    }

    /**
     * Check if any images in selectionBunch are missing locally
     */
    private boolean checkAnySelectionBunchImagesMissing(List<selectionBunchModel> selectionBunch) {
        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return true; // If no images, consider them missing
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null || TextUtils.isEmpty(bunch.getFileName())) {
                continue; // Skip if no filename
            }

            File customFolder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
            }

            // Decode URL-encoded characters in filename
            String decodedFileName = bunch.getFileName();
            try {
                decodedFileName = java.net.URLDecoder.decode(bunch.getFileName(), "UTF-8");
                Log.d("bunch###", "Decoded filename for missing check: " + bunch.getFileName() + " -> " + decodedFileName);
            } catch (Exception e) {
                Log.w("bunch###", "Failed to decode filename for missing check: " + bunch.getFileName(), e);
            }

            // Remove any subdirectory prefixes like "chats/" from the filename
            String cleanFileName = decodedFileName;
            if (cleanFileName.contains("/")) {
                cleanFileName = cleanFileName.substring(cleanFileName.lastIndexOf("/") + 1);
                Log.d("bunch###", "Removed subdirectory prefix: " + decodedFileName + " -> " + cleanFileName);
            }

            // Try both original filename and cleaned filename to match the stored format
            String originalFileName = cleanFileName;
            String sanitizedFileName = cleanFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            
            // Try original filename first
            String localPath = customFolder.getAbsolutePath() + "/" + originalFileName;
            boolean fileExists = doesFileExist(localPath);
            
            // If not found with original name, try sanitized name
            if (!fileExists && !originalFileName.equals(sanitizedFileName)) {
                localPath = customFolder.getAbsolutePath() + "/" + sanitizedFileName;
                fileExists = doesFileExist(localPath);
                Log.d("bunch###", "Trying sanitized filename: " + sanitizedFileName);
            }

            if (!fileExists) {
                Log.d("SelectionBunch", "Image not found locally: " + localPath);
                Log.d("bunch###", "Image not found locally: " + localPath);
                return true; // Found at least one missing image
            }
        }

        return false; // All images exist
    }

    /**
     * Check if selectionBunch images are from local storage (Environment.DIRECTORY_DOCUMENTS/Enclosure/Media/Images)
     */
    private boolean checkIfSelectionBunchFromLocalStorage(List<selectionBunchModel> selectionBunch) {
        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return false; // If no images, not from local storage
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null || TextUtils.isEmpty(bunch.getFileName())) {
                continue; // Skip if no filename
            }

            // Check if the image URL or filename indicates it's from local storage
            String fileName = bunch.getFileName();
            String imgUrl = bunch.getImgUrl();

            // If imgUrl has a Firebase URL, it's NOT from local storage anymore
            if (!TextUtils.isEmpty(imgUrl) && (imgUrl.contains("firebase") || imgUrl.contains("googleapis") || imgUrl.startsWith("https://"))) {
                Log.d("bunch###", "Image uploaded to Firebase (not local storage): " + imgUrl);
                return false; // This image is uploaded, not local
            }

            // If imgUrl is null or empty, it's likely from local storage
            if (TextUtils.isEmpty(imgUrl)) {
                Log.d("bunch###", "Image from local storage (no URL): " + fileName);
                return true;
            }

            // Check if the URL is a local file path
            if (imgUrl.startsWith("file://") || imgUrl.startsWith("/")) {
                Log.d("bunch###", "Image from local storage (file path): " + imgUrl);
                return true;
            }

            // Check if the filename suggests it's from local storage directory AND no Firebase URL
            if ((fileName.contains("Enclosure/Media/Images") || fileName.contains("Enclosure%2FMedia%2FImages")) && TextUtils.isEmpty(imgUrl)) {
                Log.d("bunch###", "Image from local storage (filename contains path, no URL): " + fileName);
                return true;
            }
        }

        return false; // Not from local storage
    }

    /**
     * Download missing images in selectionBunch
     */
    private void downloadAllSelectionBunchImages(senderViewHolder holder, group_messageModel model, int position) {
        Log.d("SelectionBunch", "=== STARTING DOWNLOAD ALL SELECTION BUNCH IMAGES (SENDER) ===");
        Log.d("SelectionBunch", "MessageId: " + model.getModelId());

        if (model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) {
            Log.w("SelectionBunch", "No selectionBunch to download");
            return;
        }

        Log.d("SelectionBunch", "Total images in selectionBunch: " + model.getSelectionBunch().size());

        // Filter to only missing images
        List<selectionBunchModel> missingImages = getMissingSelectionBunchImages(model.getSelectionBunch());

        if (missingImages.isEmpty()) {
            Log.d("SelectionBunch", "No missing images to download - all images already exist locally");
            holder.downlaodImgBunch.setVisibility(View.GONE);
            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
            return;
        }

        Log.d("SelectionBunch", "Found " + missingImages.size() + " missing images out of " + model.getSelectionBunch().size() + " total");
        Log.d("SelectionBunch", "Missing images details:");
        for (int i = 0; i < missingImages.size(); i++) {
            selectionBunchModel bunch = missingImages.get(i);
            Log.d("SelectionBunch", "  Missing " + i + ": " + bunch.getFileName() + " (URL: " +
                    (TextUtils.isEmpty(bunch.getImgUrl()) ? "EMPTY" : "HAS_URL") + ")");
        }

        // Show progress and hide download button immediately
        holder.downlaodImgBunch.setVisibility(View.GONE);  // Hide download button immediately
        holder.downloadPercentageImageSenderBunch.setVisibility(View.VISIBLE);
        holder.downloadPercentageImageSenderBunch.setText("0%");

        // Create download task for each missing image
        int totalMissingImages = missingImages.size();
        final int[] downloadedCount = {0};

        for (selectionBunchModel bunch : missingImages) {
            if (bunch == null || TextUtils.isEmpty(bunch.getImgUrl())) {
                continue;
            }

            // Download each missing image
            downloadSelectionBunchImage(bunch, new DownloadCallback() {
                @Override
                public void onProgress(int progress) {
                    // Update progress for this specific image
                    int overallProgress = (downloadedCount[0] * 100 + progress) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");
                        Log.d("SelectionBunch", "Updated progress: " + overallProgress + "%");
                    });
                }

                @Override
                public void onSuccess() {
                    downloadedCount[0]++;
                    int overallProgress = (downloadedCount[0] * 100) / totalMissingImages;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.downloadPercentageImageSenderBunch.setText(overallProgress + "%");

                        if (downloadedCount[0] >= totalMissingImages) {
                            // All missing images downloaded
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                            Log.d("SelectionBunch", "All missing selectionBunch images downloaded successfully");

                            // Refresh the view to show downloaded images
                            notifyItemChanged(position);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Log.e("SelectionBunch", "Failed to download image: " + error);
                    // Continue with other images even if one fails
                    downloadedCount[0]++;
                    // Update UI on main thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("SelectionBunch", "Image download failed, continuing with others: " + downloadedCount[0] + "/" + totalMissingImages);
                        if (downloadedCount[0] >= totalMissingImages) {
                            Log.d("SelectionBunch", "All download attempts completed, hiding download button");
                            holder.downlaodImgBunch.setVisibility(View.GONE);
                            holder.downloadPercentageImageSenderBunch.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    /**
     * Download a single selectionBunch image
     */
    private void downloadSelectionBunchImage(selectionBunchModel bunch, DownloadCallback callback) {
        if (bunch == null || TextUtils.isEmpty(bunch.getImgUrl())) {
            callback.onError("Invalid image URL");
            return;
        }

        // --- Prepare filename ---
        String fileName = bunch.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            fileName = extractFileNameFromFirebaseUrl(bunch.getImgUrl());
            if (TextUtils.isEmpty(fileName)) {
                fileName = "selectionBunch_" + System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            } catch (Exception ignored) {
            }
            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }
        }

        final String safeFileName = fileName; // ✅ make final for lambda
        final String mimeType = "image/jpeg";

        new Thread(() -> {
            java.net.HttpURLConnection connection = null;
            java.io.InputStream input = null;
            java.io.OutputStream publicOut = null;
            java.io.OutputStream privateOut = null;

            try {
                java.net.URL url = new java.net.URL(bunch.getImgUrl());
                connection = (java.net.HttpURLConnection) url.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != java.net.HttpURLConnection.HTTP_OK) {
                    callback.onError("HTTP Error: " + responseCode);
                    return;
                }

                input = connection.getInputStream();
                long contentLength = connection.getContentLength();

                Uri imageUri = null;

                // ✅ PUBLIC GALLERY STORAGE (for Android 10+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, safeFileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Enclosure/Media/Enclosure");
                    values.put(MediaStore.Images.Media.IS_PENDING, 1);

                    ContentResolver resolver = mContext.getContentResolver();
                    imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (imageUri == null) {
                        callback.onError("Failed to create MediaStore entry");
                        return;
                    }

                    publicOut = resolver.openOutputStream(imageUri);
                } else {
                    // ✅ For Android 9 and below (legacy public path)
                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File customDir = new File(picturesDir, "Enclosure/Media/Enclosure");
                    if (!customDir.exists()) customDir.mkdirs();

                    File imageFile = new File(customDir, safeFileName);
                    publicOut = new FileOutputStream(imageFile);
                    imageUri = Uri.fromFile(imageFile);
                }

                // ✅ PRIVATE APP STORAGE
                File privateDir;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    privateDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
                } else {
                    privateDir = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
                }
                if (!privateDir.exists()) privateDir.mkdirs();

                File privateFile = new File(privateDir, safeFileName);
                privateOut = new FileOutputStream(privateFile);

                // --- Write to both outputs (Gallery + Private) ---
                byte[] buffer = new byte[4096];
                int bytesRead;
                long total = 0;

                while ((bytesRead = input.read(buffer)) != -1) {
                    if (publicOut != null) publicOut.write(buffer, 0, bytesRead);
                    if (privateOut != null) privateOut.write(buffer, 0, bytesRead);
                    total += bytesRead;

                    if (contentLength > 0) {
                        int progress = (int) ((total * 100) / contentLength);
                        callback.onProgress(progress);
                    }
                }

                if (publicOut != null) publicOut.flush();
                if (privateOut != null) privateOut.flush();

                // ✅ Finalize (for Android Q+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && imageUri != null) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    mContext.getContentResolver().update(imageUri, values, null, null);
                }

                // ✅ Refresh gallery (for Android 9 and below)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && imageUri != null) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(imageUri);
                    mContext.sendBroadcast(mediaScanIntent);
                }

                Log.d("SelectionBunch", "Saved to gallery: " + imageUri);
                Log.d("SelectionBunch", "Saved privately: " + privateFile.getAbsolutePath());

                // ✅ Notify success to hide progress and button
                callback.onSuccess();

            } catch (Exception e) {
                Log.e("SelectionBunch", "Error downloading image: " + e.getMessage(), e);
                callback.onError(e.getMessage());
            } finally {
                try {
                    if (input != null) input.close();
                    if (publicOut != null) publicOut.close();
                    if (privateOut != null) privateOut.close();
                    if (connection != null) connection.disconnect();
                } catch (Exception ignored) {
                }
            }
        }).start();
    }




    private String extractFileNameFromFirebaseUrl(String firebaseUrl) {
        if (TextUtils.isEmpty(firebaseUrl)) {
            return null;
        }

        try {
            // Firebase Storage URLs typically look like:
            // https://firebasestorage.googleapis.com/v0/b/bucket/o/path%2Ffilename.jpg?alt=media&token=...
            // We need to extract the filename from the path part

            java.net.URL url = new java.net.URL(firebaseUrl);
            String path = url.getPath();

            // Remove the leading "/v0/b/bucket/o/" part
            if (path.startsWith("/v0/b/")) {
                int startIndex = path.indexOf("/o/");
                if (startIndex != -1) {
                    path = path.substring(startIndex + 3); // Remove "/o/"
                }
            }

            // URL decode the path
            path = java.net.URLDecoder.decode(path, "UTF-8");

            // Extract just the filename (after the last slash)
            if (path.contains("/")) {
                path = path.substring(path.lastIndexOf("/") + 1);
            }

            // If we have a valid filename, return it
            if (!TextUtils.isEmpty(path) && path.contains(".")) {
                Log.d("SelectionBunch", "Extracted filename from Firebase URL: " + firebaseUrl + " -> " + path);
                return path;
            }

        } catch (Exception e) {
            Log.w("SelectionBunch", "Failed to extract filename from Firebase URL: " + firebaseUrl, e);
        }

        return null;
    }
    /**
     * Get list of missing images from selectionBunch
     */
    private List<selectionBunchModel> getMissingSelectionBunchImages(List<selectionBunchModel> selectionBunch) {
        List<selectionBunchModel> missingImages = new ArrayList<>();

        if (selectionBunch == null || selectionBunch.isEmpty()) {
            return missingImages;
        }

        for (selectionBunchModel bunch : selectionBunch) {
            if (bunch == null || TextUtils.isEmpty(bunch.getFileName())) {
                continue; // Skip if no filename
            }

            File customFolder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                customFolder = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Enclosure/Media/Images");
            } else {
                customFolder = new File(mContext.getExternalFilesDir(null), "Enclosure/Media/Images");
            }

            String localPath = customFolder.getAbsolutePath() + "/" + bunch.getFileName();

            if (!doesFileExist(localPath)) {
                Log.d("SelectionBunch", "Missing image found: " + localPath);
                missingImages.add(bunch);
            }
        }

        return missingImages;
    }

    /**
     * Callback interface for download progress
     */
    private interface DownloadCallback {
        void onProgress(int progress);
        void onSuccess();
        void onError(String error);
    }


    private void bindSelectionBunchImagesSenderLong(
            group_messageModel model,
            RequestOptions requestOptions,
            int position,
            boolean loadHighQuality, ShapeableImageView img1,ShapeableImageView img2,ShapeableImageView img3,ShapeableImageView img4,FrameLayout img4Lyt,ImageView videoicon,TextView overlayTextImg) {

        Log.d("SelectionBunch", "bindSelectionBunchImages called for messageId=" + model.getModelId() +
                ", selectionCount=" + model.getSelectionCount() +
                ", selectionBunch=" + (model.getSelectionBunch() != null ? "not null" : "null") +
                ", selectionBunch size=" + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

        if (model.getSelectionBunch() == null) {
            Log.w("SelectionBunch", "selectionBunch is null for messageId=" + model.getModelId());
            return;
        }

        if (model.getSelectionBunch().size() < 2) {
            Log.w("SelectionBunch", "selectionBunch size=" + model.getSelectionBunch().size() + " (need >=2) for messageId=" + model.getModelId());
            return;
        }

        // Check if any images in selectionBunch are missing locally
        boolean anyImagesMissing = checkAnySelectionBunchImagesMissing(model.getSelectionBunch());

        if (anyImagesMissing) {


            Log.d("SelectionBunch", "Some images missing locally, showing download views for messageId=" + model.getModelId());
        } else {
            // Hide download views when all images exist


            Log.d("SelectionBunch", "All images exist locally, hiding download views for messageId=" + model.getModelId());
        }

        // Set width to 125dp for all selectionBunch images (only if changed to avoid layout thrash)
        float widthInDp = 125f;
        int widthInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp,
                img1.getResources().getDisplayMetrics()
        );

        // Set standard width for all images first
        ViewGroup.LayoutParams params1 = img1.getLayoutParams();
        if (params1 != null) {
            params1.width = widthInPx;
            img1.setLayoutParams(params1);
        }

        ViewGroup.LayoutParams params2 = img2.getLayoutParams();
        if (params2 != null) {
            params2.width = widthInPx;
            img2.setLayoutParams(params2);
        }

        ViewGroup.LayoutParams params3 = img3.getLayoutParams();
        if (params3 != null) {
            params3.width = widthInPx;
            img3.setLayoutParams(params3);
        }

        ViewGroup.LayoutParams params4 = img4.getLayoutParams();
        if (params4 != null) {
            params4.width = widthInPx;
            img4.setLayoutParams(params4);
        }

        ViewGroup img1Parent = (ViewGroup) img1.getParent();
        ViewGroup img3Parent = (ViewGroup) img3.getParent();
        ViewGroup img4Parent = (ViewGroup) img4.getParent();

        // Load first image into img1
        loadSelectionImageIntoViewForBunch(
                mContext,
                model.getSelectionBunch().get(0),
                model,
                requestOptions,
                img1,
                img1Parent,
                position,
                true,
                videoicon
        );

        // Load second image into img3
        loadSelectionImageIntoViewForBunch(
                mContext,
                model.getSelectionBunch().get(1),
                model,
                requestOptions,
                img3,
                img3Parent,
                position,
                true,
                videoicon
        );

        // Load third image into img4 if selectionCount is "3" or "4"
        if (model.getSelectionCount().equals("3") || model.getSelectionCount().equals("4")) {
            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(2),
                    model,
                    requestOptions,
                    img4,
                    img4Parent,
                    position,
                    true,
                    videoicon
            );
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=3
        if (model.getSelectionCount().equals("3")) {
            float img1HeightInDp = 251.5f;
            float otherHeightInDp = 125f;

            int img1HeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    img1HeightInDp,
                    img1.getResources().getDisplayMetrics()
            );

            int otherHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    otherHeightInDp,
                    img1.getResources().getDisplayMetrics()
            );

            // Force set img1 dimensions
            ViewGroup.LayoutParams finalParams1 = img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = img1HeightInPx;
                img1.setLayoutParams(finalParams1);
                Log.d("SelectionBunch", "FORCE Set img1 to " + widthInPx + "x" + img1HeightInPx + "px");
            }

            // Force set img3 dimensions
            ViewGroup.LayoutParams finalParams3 = img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = otherHeightInPx;
                img3.setLayoutParams(finalParams3);
                Log.d("SelectionBunch", "FORCE Set img3 to " + widthInPx + "x" + otherHeightInPx + "px");
            }

            // Force set img4 dimensions
            ViewGroup.LayoutParams finalParams4 = img4.getLayoutParams();
            if (finalParams4 != null) {
                finalParams4.width = widthInPx;
                finalParams4.height = otherHeightInPx;
                img4.setLayoutParams(finalParams4);
                Log.d("SelectionBunch", "FORCE Set img4 to " + widthInPx + "x" + otherHeightInPx + "px");
            }

            // Set corner radius for img4 (bottom-right only)
            float cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    20,
                    img4.getResources().getDisplayMetrics()
            );

            ShapeAppearanceModel shapeModel4 = img4.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
                    .setTopRightCorner(CornerFamily.ROUNDED, 0f)
                    .setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
                    .build();

            img4.setShapeAppearanceModel(shapeModel4);
            Log.d("SelectionBunch", "Set img4 corner radius: bottom-right 20dp");
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=2
        if (model.getSelectionCount().equals("2")) {
            float allHeightInDp = 251.5f;
            int allHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    allHeightInDp,
                    img1.getResources().getDisplayMetrics()
            );

            // Force set img1 and img3 to 125dp x 125dp
            ViewGroup.LayoutParams finalParams1 = img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = allHeightInPx;
                img1.setLayoutParams(finalParams1);
            }

            ViewGroup.LayoutParams finalParams3 = img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = allHeightInPx;
                img3.setLayoutParams(finalParams3);
            }

            Log.d("SelectionBunch", "FORCE Set img1 and img3 for selectionCount=2 to " + widthInPx + "x" + allHeightInPx + "px");
        }

        // FORCE set dimensions AFTER images are loaded for selectionCount=4
        if (model.getSelectionCount().equals("4")) {
            float allHeightInDp = 125.5f;
            int allHeightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    allHeightInDp,
                    img1.getResources().getDisplayMetrics()
            );

            // Force set all images to 125dp x 125dp
            ViewGroup.LayoutParams finalParams1 = img1.getLayoutParams();
            if (finalParams1 != null) {
                finalParams1.width = widthInPx;
                finalParams1.height = allHeightInPx;
                img1.setLayoutParams(finalParams1);
                Log.d("SelectionBunch", "FORCE Set img1 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            Log.d("SelectionBunch", "=== FORCE IMG2 DIMENSIONS START for selectionCount=4 ===");
            Log.d("SelectionBunch", "img2 before force - visibility: " + img2.getVisibility() +
                    ", width: " + img2.getWidth() +
                    ", height: " + img2.getHeight());

            ViewGroup.LayoutParams finalParams2 = img2.getLayoutParams();
            if (finalParams2 != null) {
                Log.d("SelectionBunch", "img2 force layoutParams before: " + finalParams2.width + "x" + finalParams2.height);
                finalParams2.width = widthInPx;
                finalParams2.height = allHeightInPx;
                img2.setLayoutParams(finalParams2);

                // Force layout pass to apply the new dimensions
                img2.requestLayout();
                img2.invalidate();

                Log.d("SelectionBunch", "FORCE Set img2 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
                Log.d("SelectionBunch", "img2 after force - width: " + img2.getWidth() +
                        ", height: " + img2.getHeight());
            } else {
                Log.e("SelectionBunch", "img2 force layoutParams is NULL!");
            }

            ViewGroup.LayoutParams finalParams3 = img3.getLayoutParams();
            if (finalParams3 != null) {
                finalParams3.width = widthInPx;
                finalParams3.height = allHeightInPx;
                img3.setLayoutParams(finalParams3);
                Log.d("SelectionBunch", "FORCE Set img3 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            ViewGroup.LayoutParams finalParams4 = img4.getLayoutParams();
            if (finalParams4 != null) {
                finalParams4.width = widthInPx;
                finalParams4.height = allHeightInPx;
                img4.setLayoutParams(finalParams4);
                Log.d("SelectionBunch", "FORCE Set img4 for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");
            }

            Log.d("SelectionBunch", "FORCE Set all images for selectionCount=4 to " + widthInPx + "x" + allHeightInPx + "px");

            img2.setVisibility(View.VISIBLE);


            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(3),
                    model,
                    requestOptions,
                    img2,
                    img4Parent,
                    position,
                    true,
                    videoicon
            );
        }

        // Load fourth image into img2 if selectionCount is "4"
        Log.d("SelectionBunch", "=== IMG2 LOADING CHECK START ===");
        Log.d("SelectionBunch", "selectionCount: " + model.getSelectionCount() +
                ", bunchSize: " + (model.getSelectionBunch() != null ? model.getSelectionBunch().size() : 0));

        if (model.getSelectionBunch().size() > 4) {
            Log.d("SelectionBunch", "=== LOADING IMG2 for selectionCount=4 ===");
            Log.d("SelectionBunch", "IMG2 URL: " + model.getSelectionBunch().get(3).getImgUrl());
            Log.d("SelectionBunch", "IMG2 FILE: " + model.getSelectionBunch().get(3).getFileName());
            Log.d("SelectionBunch", "IMG2 DEBUG START");
            Log.d("SelectionBunch", "IMG2 TEST 1");
            System.out.println("IMG2 TEST 1 - System.out.println");

            ViewGroup img2Parent = (ViewGroup) img2.getParent();
            Log.d("SelectionBunch", "img2 parent: " + (img2Parent != null ? img2Parent.getClass().getSimpleName() : "NULL"));

            // Check img2 state before loading
            Log.d("SelectionBunch", "img2 before loading - visibility: " + img2.getVisibility() +
                    ", width: " + img2.getWidth() +
                    ", height: " + img2.getHeight() +
                    ", alpha: " + img2.getAlpha() +
                    ", scaleX: " + img2.getScaleX() +
                    ", scaleY: " + img2.getScaleY());

            Log.d("SelectionBunch", "Calling loadSelectionImageIntoViewForBunch for img2...");
            Log.d("SelectionBunch", "IMG2 TEST 2");
            System.out.println("IMG2 TEST 2 - System.out.println");
            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(0),
                    model,
                    requestOptions,
                    img1,
                    img2Parent,
                    position,
                    true,
                    videoicon
            );



            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(1),
                    model,
                    requestOptions,
                    img2,
                    img2Parent,
                    position,
                    true,
                    videoicon
            );


            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(2),
                    model,
                    requestOptions,
                    img3,
                    img2Parent,
                    position,
                    true,
                    videoicon
            );

            loadSelectionImageIntoViewForBunch(
                    mContext,
                    model.getSelectionBunch().get(3),
                    model,
                    requestOptions,
                    img4,
                    img2Parent,
                    position,
                    true,
                    videoicon
            );

            overlayTextImg.setVisibility(View.VISIBLE);


            Log.d("SelectionBunch", "Completed loading img2 for selectionCount=4");
            Log.d("SelectionBunch", "IMG2 TEST 3");
            System.out.println("IMG2 TEST 3 - System.out.println");

            // Additional debugging for img2 state
            Log.d("SelectionBunch", "img2 after loading - visibility: " + img2.getVisibility() +
                    ", width: " + img2.getWidth() +
                    ", height: " + img2.getHeight() +
                    ", alpha: " + img2.getAlpha() +
                    ", scaleX: " + img2.getScaleX() +
                    ", scaleY: " + img2.getScaleY() +
                    ", layoutParams: " + img2.getLayoutParams());
            Log.d("SelectionBunch", "img2 drawable: " + (img2.getDrawable() != null ? "NOT NULL" : "NULL"));

            // Final comprehensive check
            Log.d("SelectionBunch", "IMG2 FINAL: V=" + img2.getVisibility() + " W=" + img2.getWidth() + " H=" + img2.getHeight());
            Log.d("SelectionBunch", "IMG2 FINAL: Alpha=" + img2.getAlpha() + " Scale=" + img2.getScaleX() + " Drawable=" + (img2.getDrawable() != null));
            Log.d("SelectionBunch", "IMG2 FINAL: Shown=" + img2.isShown());
        }

        // Set visibility based on selection count
        Log.d("SelectionBunch", "=== VISIBILITY SETTING START ===");
        Log.d("SelectionBunch", "Setting visibility for selectionCount: " + model.getSelectionCount());

        if (model.getSelectionCount().equals("2")) {
            Log.d("SelectionBunch", "Setting img2 to GONE for selectionCount=2");
            img2.setVisibility(View.GONE);
            img4.setVisibility(View.GONE);
            img4Lyt.setVisibility(View.GONE);
        } else if (model.getSelectionCount().equals("3")) {
            Log.d("SelectionBunch", "Setting img2 to GONE for selectionCount=3");
            img2.setVisibility(View.GONE);
            img4.setVisibility(View.VISIBLE);
            img4Lyt.setVisibility(View.VISIBLE);
        } else
        if (model.getSelectionCount().equals("4")) {
            Log.d("SelectionBunch", "Setting img2 to VISIBLE for selectionCount=4");
            Log.d("SelectionBunch", "img2 before visibility set - visibility: " + img2.getVisibility());
            img2.setVisibility(View.VISIBLE);
            Log.d("SelectionBunch", "img2 after visibility set - visibility: " + img2.getVisibility());
            img4.setVisibility(View.VISIBLE);
            img4Lyt.setVisibility(View.VISIBLE);
        }

        Log.d("SelectionBunch", "Final img2 state - visibility: " + img2.getVisibility() +
                ", width: " + img2.getWidth() +
                ", height: " + img2.getHeight());

    }



    private void saveImageToGallery(group_messageModel model) {
        new Thread(() -> {
            try {
                String imageUrl = model.getDocument(); // remote or local
                String fileName = model.getFileName();
                Bitmap bitmap = null;

                // 1️⃣ Try local file
                File localFile = new File(imageUrl);
                if (localFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }

                // 2️⃣ Try network download if needed
                if (bitmap == null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
                    try {
                        bitmap = Picasso.get().load(imageUrl).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastSafe("Failed to download image");
                        return;
                    }
                }

                // 3️⃣ Save image to public gallery
                if (bitmap != null) {
                    saveBitmapToGallery(bitmap, fileName);
                } else {
                    showToastSafe("Image not found");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showToastSafe("Error saving image");
            }
        }).start();
    }

    private void saveBitmapToGallery(Bitmap bitmap, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Enclosure");
                values.put(MediaStore.MediaColumns.IS_PENDING, 1);

                Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (OutputStream out = resolver.openOutputStream(uri)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    }

                    values.clear();
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                    resolver.update(uri, values, null, null);

                    showToastSafe("Image saved to Gallery");
                } else {
                    showToastSafe("Failed to save image");
                }
            } else {
                // Android 9 and below
                File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File enclosureDir = new File(picturesDir, "Enclosure");
                if (!enclosureDir.exists()) enclosureDir.mkdirs();

                File imageFile = new File(enclosureDir, fileName);
                try (FileOutputStream out = new FileOutputStream(imageFile)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                }

                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(imageFile));
                mContext.sendBroadcast(scanIntent);

                showToastSafe("Image saved to Gallery");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToastSafe("Failed to save image");
        }
    }
    private void showToastSafe(String message) {
//        new Handler(Looper.getMainLooper()).post(() ->
//              //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
//        );
    }



}