package com.Appzia.enclosure.Utils;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;

import static com.Appzia.enclosure.Screens.chattingScreen.playerPreview;
import static com.Appzia.enclosure.Screens.grpChattingScreen.playerPreview2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.contactGetModel;
import com.Appzia.enclosure.Model.flagModel;
import com.Appzia.enclosure.Model.get_calling_contact_list_model;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vdurmont.emoji.EmojiParser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;

//import live.videosdk.rtc.android.Participant;

public class Constant {

    // TODO: 11/09/24 BACKGROUND VALUEXXX --------------------------

    public static final String firstKeyxxx = "firstKeyxxx";
    public static final String participantIdxxx = "participantIdxxx";
    public static final String tokenxxx = "tokenxxx";
    public static final String meetingIdxxx = "meetingIdxxx";
    public static final String callerKeyxxx = "callerKeyxxx";
    public static final String nameKeyxxx = "nameKeyxxx";
    public static final String photoKeyxxx = "photoKeyxxx";
    public static final String playKeyxxx = "playKeyxxx";
    public static final String fTokenKeyxxx = "fTokenKeyxxx";
    public static final String currentTimexxx = "currentTimexxx";
    public static final String keepTrackKeyxxx = "keepTrackKeyxxx";

    // TODO: 11/09/24  --------------------------------------------------


    // TODO: 13/09/24 BACKGROUND VALUEXXX for video--------------------------

    public static final String myRoomXXX = "myRoomXXX";
    public static final String usernameXXX = "usernameXXX";
    public static final String incomingXXX = "incomingXXX";
    public static final String createdByXXX = "createdByXXX";
    public static final String roomFlagKeyXXX = "roomFlagKeyXXX";
    public static final String photoReceiverXXX = "photoReceiverXXX";
    public static final String nameReceiverXXX = "nameReceiverXXX";

    public static final String playKeyxxxVideo = "playKeyxxxVideo";
    public static final String fTokenKeyxxxVideo = "fTokenKeyxxxVideo";
    public static final String currentTimexxxVideo = "currentTimexxxVideo";
    public static final String keepTrackKeyxxxVideo = "keepTrackKeyxxxVideo";

    // TODO: 13/09/24  --------------------------------------------------


    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String last_deleted_model_id = "last_deleted_model_id";
    public static final String GRPKRY = "groupKey";
    public static final String MEETING_JOINED = "MEETING_JOINED";
    public static final String doc = "doc";
    public static final String camera = "camera";

    public static String msg_limitFORALL = "msg_limitFORALL";
    public static String photoReceiver = "photoReceiver";
    public static String participantsXXX = "participantsXXX";
    public static String photoEmergency="photoEmergency";
    public static String nameEmergency="nameEmergency";
    public static String callPhoto="callPhoto";
    public static String audioSet="audioSet";

    static boolean includeScaleDown = true;
    public static final String INCOMING_VIDEO_CALL = "incomingvideocall";
    public static final String INCOMING_VOICE_CALL = "incomingvoicecall";

    //    public static ArrayList<Participant> partList = new ArrayList<>();
    public static ArrayList<call_log_history_model> call_log_history_list = new ArrayList<>();
    public static final String ACCEPT_INCOMING_CALL = "acceptincomingcall";

    public static String ChannelName = "EnclosureCNL";
    //video sdk token key
    public static final String VIDEOSDKTOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJiNjBmZDhlNS05MjE2LTRkZTAtYTEzMy05ZjliNDkyZWIwMTAiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTcwNjMzNTc4OCwiZXhwIjoxODY0MTIzNzg4fQ.dTdgpXek9rZamSfNLk-_QJWVCwj0lzZ3O5YsfeQbzvU";
    //Enclosure 2023 update
    public static AnimatorSet animatorSet = new AnimatorSet();
    public static final String UID_KEY = "UID_KEY";
    public static final String callDrop = "Call drop";

    public static View viewShape;
    public static final String PHONE_NUMBERKEY = "PHONE_NUMBER";
    public static View dialogLayoutFullScreenView;
    public static WindowManager.LayoutParams dialogLayoutFullScreenParams;
    public static WindowManager dialogLayoutFullScreenWindowManager;
    public static ArrayList<flagModel> flagModelList = new ArrayList<>();
    public static String large = "large";
    public static String medium = "medium";
    public static String small = "small";
    public static String Font_Size = "Font_Size";
    public static String loggedInKey = "loggedInKey";
    public static String BACKKEY = "BACKKEY";
    public static NetworkInfo wifiInfo, mobileInfo;
    public static BottomSheetDialog bottomSheetDialog;
    public static BottomSheetDialog bottomSheetDialogContact;
    public static Dialog dialogForNetwork;
    public static ConnectivityManager connectivityManager;
    public static String chattingBackValue = "chattingBackValue";
    public static String otherBackValue = "otherBackValue";

    public static View dialogView;

    public static ArrayList<profilestatusModel> profilestatusList = new ArrayList<>();

    public static String lock3 = "lock3";
    public static ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list = new ArrayList<>();
    public static ArrayList<get_user_active_contact_list_Model> get_user_active_contact_forward_list = new ArrayList<>();
    public static ArrayList<get_user_active_contact_list_MessageLmt_Model> get_user_active_contact_listmsgLmt = new ArrayList<>();
    public static ArrayList<String> searchMsgLmt = new ArrayList<>();
    public static ArrayList<String> searchVoiceCall = new ArrayList<>();
    public static ArrayList<String> searchVideoCall = new ArrayList<>();

    public static String img = "img";
    public static String video = "video";
    public static String Text = "Text";
    public static String contact = "contact";
    public static String voiceAudio = "voiceAudio";

    public static Dialog dialogLayoutColor;
    public static Dialog dialogLayoutFullScreen;
    public static String profilePic = "profilePic";
    public static String FCM_TOKEN = "FCM_TOKEN";
    public static String full_name = "full_name";
    public static String callName = "callName";
    public static ArrayList<get_calling_contact_list_model> get_calling_contact_list = new ArrayList<>();
    public static ArrayList<get_contact_model> get_contact_model = new ArrayList<>();
    public static String forwordKey = "forwordKey";
    public static String groupKey = "groupKey";
    public static String sleepKey = "sleepKey";
    public static String voiceRadioKey = "voiceRadioKey";
    public static String videoRadioKey = "videoRadioKey";
    public static String startMsgKey = "startMsgKey";
    public static String startDownArrowKey = "startDownArrowKey";
    public static String tapOnNotification = "tapOnNotification";
    public static String chattingSocket = "chattingSocket";
    public static String AVAILABLE = "AVAILABLE";
    public static String BUSY = "BUSY";
    public static String senderViewHolder = "senderViewHolder";
    public static String receiverViewHolder = "receiverViewHolder";

    static Vibrator vibrator;
    public static SharedPreferences.Editor setSF;
    public static SharedPreferences getSF;
    public static String ThemeColorKey = "ThemeColorKey";
    public static String name1 = "Joey Banks";
    public static String name2 = "Ram cooks";

    public static LinearLayout deleteLyt;
    public static ArrayList<contactGetModel> contactGetList = new ArrayList<>();


    // Area Reserve for firebase constant only
    public static String CHAT = "chats";
    public static String GROUPCHAT = "group_chats";
    public static ArrayList<messageModel> messageList = new ArrayList<>();
    public static String voicecall = "Incoming voice call";
    public static String chatting = "chatting";
    public static String videoMissCall = "videoMissCall";
    public static String voiceMissCall = "voiceMissCall";
    public static String videocall = "Incoming video call";
    public static String ReplyKey = "ReplyKey";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void Vibrator(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) mContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            Vibrator vibrator = vibratorManager.getDefaultVibrator();
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
            }
        } else {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void Vibrator50(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK));
        }
    }

    public static void vibrateOneSecond(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Android 8.0+ साठी
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void VibratorLowBeam(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) mContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            Vibrator vibrator = vibratorManager.getDefaultVibrator();
            vibrator.vibrate(VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            Vibrator v = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }


    public static void setSfFunction(Context context) {
/*  public static SharedPreferences.Editor setSF;
    public static SharedPreferences getSF;*/
        SharedPreferences sharedPreferences = context.getSharedPreferences("ENCLOSURE_SHARED", MODE_PRIVATE);
        setSF = sharedPreferences.edit();
    }

    public static void getSfFuncion(Context context) {
        getSF = context.getSharedPreferences("ENCLOSURE_SHARED", MODE_PRIVATE);

    }


    public static void NetworkCheckDialogue(Context mContext) {

        dialogForNetwork = new Dialog(mContext);
        ((android.app.Dialog) dialogForNetwork).setContentView(R.layout.network_dialogue);
        dialogForNetwork.setCanceledOnTouchOutside(true);
        dialogForNetwork.setCancelable(true);
        dialogForNetwork.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        dialogForNetwork.getWindow().setGravity(Gravity.CENTER);
        dialogForNetwork.getWindow().setBackgroundDrawable(null);

        Window window = dialogForNetwork.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    public static void ObjectAnimator(View view, float x, float y, float alpha2) {
        animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, x);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, y);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, alpha2);
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(200);
    }


    public static void dialogueLayout(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;

        dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        dialogLayoutColor.getWindow().setAttributes(layoutParams);
        Window window = Constant.dialogLayoutColor.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void dialogueLayoutUpper(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);

        // Remove default shadow by setting a transparent background
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Enable dim effect
        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f; // Set background dimming (adjust as needed)
        dialogLayoutColor.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        // Set gravity to the top right of the screen
        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);

        // Convert 50dp to pixels for top margin
        int topMarginInPixels = (int) (60 * mContext.getResources().getDisplayMetrics().density);

        // Set the top margin
        layoutParams.y = topMarginInPixels;
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        // Set window size to wrap content
        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }


    public static void dialogueLayoutSearchChatt(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog

        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);


        // Convert 50dp to pixels
        int topMarginInPixels = (int) (48 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }


    public static void dialogueLayoutCLearLogUp(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog
        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);

        // Convert 50dp to pixels
        int topMarginInPixels = (int) (120 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public static void dialogueLayoutMsgLmt(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog
        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);

        // Convert 50dp to pixels
        int topMarginInPixels = (int) (180 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }


    public static void dialogueLayoutInviteScreen(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog
        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);

        // Convert 50dp to pixels
        int topMarginInPixels = (int) (90 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public static void dialogueLayoutShowImageScreen(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog
        dialogLayoutColor.getWindow().setGravity(Gravity.TOP | Gravity.END);

        // Convert 50dp to pixels
        int topMarginInPixels = (int) (30 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public static void dialogueLayoutCLearLogBottom(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;
        dialogLayoutColor.getWindow().getAttributes().dimAmount = 0f; // Disable dimming behind the dialog
        dialogLayoutColor.getWindow().setGravity(Gravity.CENTER | Gravity.END);

        // Convert 50dp to pixels
        int topMarginInPixels = (int) (115 * mContext.getResources().getDisplayMetrics().density);

        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        layoutParams.y = topMarginInPixels; // Set top margin of 50dp
        dialogLayoutColor.getWindow().setAttributes(layoutParams);

        Window window = dialogLayoutColor.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }


    public static void dialogueLayoutForAll(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);

        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;

        dialogLayoutColor.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        dialogLayoutColor.getWindow().setAttributes(layoutParams);
        Window window = Constant.dialogLayoutColor.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void dialogueLayoutReceiver(Context mContext, int layout) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(layout);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation2;

        dialogLayoutColor.getWindow().setGravity(Gravity.CENTER | Gravity.START);
        WindowManager.LayoutParams layoutParams = dialogLayoutColor.getWindow().getAttributes();
        dialogLayoutColor.getWindow().setAttributes(layoutParams);
        Window window = Constant.dialogLayoutColor.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    public static void dialogueFullScreenOverlay(Context mContext, int layout) {
        // Create a View for the overlay dialog
        dialogView = View.inflate(mContext, layout, null);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // Define layout parameters for the overlay dialog
        WindowManager.LayoutParams layoutParams;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS // Ensure overlay covers navigation bar
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, // Ensure dialog stays in screen
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
        }

        // Set position and attributes
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.windowAnimations = R.style.PauseDialogAnimation;
        layoutParams.dimAmount = 0.5f; // Add dimming effect behind the dialog
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        // Handle back press to prevent default system back button behavior
        dialogView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                // Do nothing or add custom logic here (e.g., show a toast, perform an action)
                // Returning true consumes the back press event, preventing dialog dismissal
                return true;
            }
            return false;
        });

        // Add the dialog view to WindowManager
        windowManager.addView(dialogView, layoutParams);
        Constant.dialogLayoutFullScreenView = dialogView;
        Constant.dialogLayoutFullScreenParams = layoutParams;
        Constant.dialogLayoutFullScreenWindowManager = windowManager;
    }


    public static void bottomsheetforward(Context mContext) {
        bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog2);
        viewShape = LayoutInflater.from(mContext).inflate(R.layout.forward_layout, null, false);
        bottomSheetDialog.setContentView(viewShape);

    }

    public static void bottomsheetEmoji(Context mContext, int layout) {
        bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog4);
        viewShape = LayoutInflater.from(mContext).inflate(layout, null, false);
        bottomSheetDialog.setContentView(viewShape);

    }

    public static void bottomsheetEmojiPeople(Context mContext, int layout) {
        bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog5);
        viewShape = LayoutInflater.from(mContext).inflate(layout, null, false);
        bottomSheetDialog.setContentView(viewShape);

    }


    public static void bottomsheetContact(Context mContext, int layout) {
        bottomSheetDialogContact = new BottomSheetDialog(mContext, R.style.BottomSheetDialogContact);
        viewShape = LayoutInflater.from(mContext).inflate(layout, null, false);
        bottomSheetDialogContact.setContentView(viewShape);

    }


    public static void startSnapAnimation(View viewToAnimate) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToAnimate, View.ALPHA, 1f, 0f);
        fadeOut.setDuration(1000); // Customize duration as needed

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(viewToAnimate, View.SCALE_X, 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(viewToAnimate, View.SCALE_Y, 1f, 0f);

        ObjectAnimator rotateX = ObjectAnimator.ofFloat(viewToAnimate, View.ROTATION_X, 0f, 360f);
        rotateX.setInterpolator(new AccelerateDecelerateInterpolator()); // Apply an interpolator for smoother rotation

        ObjectAnimator rotateY = ObjectAnimator.ofFloat(viewToAnimate, View.ROTATION_Y, 0f, 360f);
        rotateY.setInterpolator(new AccelerateDecelerateInterpolator()); // Apply an interpolator for smoother rotation

        // Add more animations as needed

        AnimatorSet snapAnimatorSet = new AnimatorSet();
        snapAnimatorSet.playTogether(fadeOut, scaleX, scaleY, rotateX, rotateY);
        snapAnimatorSet.start();
    }


    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy"); // Customize format here
        return dateFormat.format(calendar.getTime());
    }

    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(calendar.getTime());
    }



    public static String analyzeTextContent(String text) {
        android.util.Log.d("TAG", "countEmojis: " + text);
        // Regex to match all emojis, including ❤️
        String emojiRegex = "[\uD83C\uDF00-\uD83E\uDFFF\uD83D\uDC00-\uD83D\uDFFF\u2600-\u26FF\u2700-\u27BF\u2300-\u23FF\u2B50\u2B06\u2194\u2B06\u21A9\u2934\u21AA\u21AA\u2935\u25AA\u23F3\u25FE\u231A\u231B\u24C2\uD83D\uDE00-\uD83D\uDE4F\uD83D\uDE80-\uD83D\uDEFF\uD83E\uDD10\uD83E\uDD11\uD83E\uDD12\uD83E\uDD13\uD83D\uDC8B\u2764\uFE0F\u00A9\u00AE]+";
        List<String> emojis = new ArrayList<>();
        Matcher matcher = Pattern.compile(emojiRegex).matcher(text);
        while (matcher.find()) {
            emojis.add(matcher.group());
        }

        String textWithoutEmojis = text.replaceAll(emojiRegex, "").trim();

        if (!emojis.isEmpty() && textWithoutEmojis.isEmpty()) {
            return "only_emoji"; // Only emojis
        } else if (!emojis.isEmpty() && !textWithoutEmojis.isEmpty()) {
            return "text_and_emoji"; // Mixed content
        } else {
            return "only_text"; // Only text
        }
    }


    public static int countEmojis(String text) {
        //  Extract all emojis from the text

        List<String> emojis = EmojiParser.extractEmojis(text);

        return emojis.size();
    }


    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }


    public static void showCustomToast(String message, CardView customToastCard, TextView customToastText) {
        customToastText.setText(message);
        customToastCard.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                customToastCard.setVisibility(View.GONE);
            }
        }, 3000);
    }


    public static void dialoguePermision(Context mContext) {
        dialogLayoutColor = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutColor).setContentView(R.layout.permission_lyt);
        dialogLayoutColor.setCanceledOnTouchOutside(true);
        dialogLayoutColor.setCancelable(true);
        dialogLayoutColor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLayoutColor.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;


    }
    public static void loadImageIntoView(Context context, String imageSource, RequestOptions requestOptions,
                                         ImageView targetImageView, ViewGroup parentLayout, int position,
                                         boolean loadHighQuality, messageModel model, View videoIcon)

    { // Changed messageModel to Message
        // Get screen density and orientation
        float density = context.getResources().getDisplayMetrics().density;
        int orientation = context.getResources().getConfiguration().orientation;

        // Log input values for debugging
        Log.d("loadImageIntoView", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoView", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoView", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoView", "Orientation: " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape"));
        Log.d("loadImageIntoView", "Load High Quality: " + loadHighQuality); // Log the quality flag

        // Parse dimensions with error handling
        float imageWidthPx;
        float imageHeightPx;
        float aspectRatio;
        try {
            String width = model.getImageWidth();
            String height = model.getImageHeight();
            String ratio = model.getAspectRatio();

            imageWidthPx = Float.parseFloat((width != null && !width.trim().isEmpty()) ? width : "300");
            imageHeightPx = Float.parseFloat((height != null && !height.trim().isEmpty()) ? height : "300");
            aspectRatio = Float.parseFloat((ratio != null && !ratio.trim().isEmpty()) ? ratio : "1.0");
            if (aspectRatio <= 0) {
                aspectRatio = imageWidthPx / imageHeightPx; // Fallback to calculated aspect ratio
            }
        } catch (NumberFormatException e) {
            Log.e("loadImageIntoView", "Invalid dimensions or aspect ratio, using defaults", e);
            imageWidthPx = 210f;
            imageHeightPx = 250f;
            aspectRatio = 1.0f;
        }

        // Define maximum dimensions in dp
        final float MAX_WIDTH_DP = 210f;
        final float MAX_HEIGHT_DP = 250f;

        // Convert max dimensions to pixels
        int maxWidthPx = (int) (MAX_WIDTH_DP * density);
        int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

        // Calculate dimensions based on orientation and aspect ratio
        int finalWidthPx;
        int finalHeightPx;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape: Prioritize width for wide images
            finalWidthPx = maxWidthPx;
            finalHeightPx = (int) (maxWidthPx / aspectRatio);
            // Ensure height doesn't exceed maxHeightPx
            if (finalHeightPx > maxHeightPx) {
                finalHeightPx = maxHeightPx;
                finalWidthPx = (int) (maxHeightPx * aspectRatio);
            }
        } else {
            // Portrait: Prioritize height for wide images
            finalHeightPx = maxHeightPx;
            finalWidthPx = (int) (maxHeightPx * aspectRatio);
            // Ensure width doesn't exceed maxWidthPx
            if (finalWidthPx > maxWidthPx) {
                finalWidthPx = maxWidthPx;
                finalHeightPx = (int) (maxWidthPx / aspectRatio);
            }
        }

        // Ensure final dimensions are within bounds
        finalWidthPx = Math.min(finalWidthPx, maxWidthPx);
        finalHeightPx = Math.min(finalHeightPx, maxHeightPx);

        // Log final dimensions

        // Set ImageView dimensions
        ViewGroup.LayoutParams params = targetImageView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(finalWidthPx, finalHeightPx);
        } else {
            params.width = finalWidthPx;
            params.height = finalHeightPx;
        }
        targetImageView.setLayoutParams(params);

        // Adjust parent layout to wrap content
        if (parentLayout != null) {
            ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
            if (parentParams != null) {
                parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentLayout.setLayoutParams(parentParams);
            }
        }

        // Simple, fast loading without complex blur transformations for better performance
        RequestBuilder<Drawable> glideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                 // ✅ Add placeholder to prevent black flash
                .error(R.drawable.inviteimg) // ✅ Add error placeholder
                .fallback(R.drawable.inviteimg); // ✅ Add fallback for null/empty URLs

        // Single quality setting for all images - 75% quality
        if (videoIcon != null) {
            videoIcon.setVisibility(View.VISIBLE);
        }
        glideRequest = glideRequest
                .override(finalWidthPx, finalHeightPx)
                .encodeQuality(75) // Single 75% quality for all images
                .format(DecodeFormat.PREFER_ARGB_8888); // Use high quality format

        glideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image/Video load failed for: " + imageSource, e);

                        // Check if this is a video decoding error
                        if (e != null && e.getCauses() != null) {
                            for (Throwable cause : e.getCauses()) {
                                if (cause instanceof RuntimeException &&
                                    cause.getMessage() != null &&
                                    cause.getMessage().contains("setDataSource failed")) {
                                    Log.w("Glide", "Video thumbnail decoding failed, likely corrupted or unsupported format: " + imageSource);
                                    // Hide video icon for failed videos
                                    if (videoIcon != null) {
                                        videoIcon.setVisibility(View.VISIBLE);
                                    }
                                    // Try to load a fallback placeholder
                                    try {
                                        targetImageView.setImageResource(android.R.drawable.ic_media_play);
                                    } catch (Exception ignored) {
                                        // If even placeholder fails, just clear the image
                                        targetImageView.setImageDrawable(null);
                                    }
                                    return true; // Handled the error
                                }
                            }
                        }

                        // For other types of errors, hide video icon and try default handling
                        if (videoIcon != null) {
                            videoIcon.setVisibility(View.VISIBLE);
                        }
                        return false; // Let Glide handle other errors normally
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image/Video loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }

    // Optimized version for group messages - same performance as individual chat
    public static void loadImageIntoViewGroupOptimized(Context context, String imageSource, RequestOptions requestOptions,
                                                      ImageView targetImageView, ViewGroup parentLayout, int position,
                                                      boolean loadHighQuality, group_messageModel model, View videoIcon) {
        // Get screen density and orientation
        float density = context.getResources().getDisplayMetrics().density;
        int orientation = context.getResources().getConfiguration().orientation;

        // Log input values for debugging
        Log.d("loadImageIntoViewGroupOptimized", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewGroupOptimized", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewGroupOptimized", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewGroupOptimized", "Orientation: " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape"));
        Log.d("loadImageIntoViewGroupOptimized", "Load High Quality: " + loadHighQuality);

        // Parse dimensions with error handling
        float imageWidthPx;
        float imageHeightPx;
        float aspectRatio;
        try {
            String width = model.getImageWidth();
            String height = model.getImageHeight();
            String ratio = model.getAspectRatio();

            imageWidthPx = Float.parseFloat((width != null && !width.trim().isEmpty()) ? width : "300");
            imageHeightPx = Float.parseFloat((height != null && !height.trim().isEmpty()) ? height : "300");
            aspectRatio = Float.parseFloat((ratio != null && !ratio.trim().isEmpty()) ? ratio : "1.0");
            if (aspectRatio <= 0) {
                aspectRatio = imageWidthPx / imageHeightPx; // Fallback to calculated aspect ratio
            }
        } catch (NumberFormatException e) {
            Log.e("loadImageIntoViewGroupOptimized", "Invalid dimensions or aspect ratio, using defaults", e);
            imageWidthPx = 210f;
            imageHeightPx = 250f;
            aspectRatio = 1.0f;
        }

        // Define maximum dimensions in dp
        final float MAX_WIDTH_DP = 210f;
        final float MAX_HEIGHT_DP = 250f;

        // Convert max dimensions to pixels
        int maxWidthPx = (int) (MAX_WIDTH_DP * density);
        int maxHeightPx = (int) (MAX_HEIGHT_DP * density);

        // Calculate final dimensions based on aspect ratio
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
            finalWidthPx = (int) (maxHeightPx * aspectRatio);
            if (finalWidthPx > maxWidthPx) {
                finalWidthPx = maxWidthPx;
                finalHeightPx = (int) (maxWidthPx / aspectRatio);
            }
        }

        // Ensure dimensions don't exceed maximums
        finalWidthPx = Math.min(finalWidthPx, maxWidthPx);
        finalHeightPx = Math.min(finalHeightPx, maxHeightPx);

        // Set layout parameters
        ViewGroup.LayoutParams params = targetImageView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(finalWidthPx, finalHeightPx);
        } else {
            params.width = finalWidthPx;
            params.height = finalHeightPx;
        }
        targetImageView.setLayoutParams(params);

        if (parentLayout != null) {
            ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
            if (parentParams != null) {
                parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentLayout.setLayoutParams(parentParams);
            }
        }

        // Simple, fast loading without complex blur transformations
        RequestBuilder<Drawable> glideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                 // ✅ Add placeholder to prevent black flash
                .error(R.drawable.inviteimg) // ✅ Add error placeholder
                .fallback(R.drawable.inviteimg); // ✅ Add fallback for null/empty URLs

        // Single quality setting for all images - 75% quality
        if (videoIcon != null) {
            videoIcon.setVisibility(View.VISIBLE);
        }
        glideRequest = glideRequest
                .override(finalWidthPx, finalHeightPx)
                .encodeQuality(75) // Single 75% quality for all images
                .format(DecodeFormat.PREFER_ARGB_8888); // Use high quality format

        glideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Group Image/Video load failed for: " + imageSource, e);
                        if (videoIcon != null) {
                            videoIcon.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Group Image/Video loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }

    // Optimized PDF loading for group messages - same performance as individual chat
    public static void loadImageIntoViewGroupOptimizedPdfGroup(Context context, String imageSource, RequestOptions requestOptions,
                                                              ImageView targetImageView, ViewGroup parentLayout, int position,
                                                              boolean loadHighQuality, group_messageModel model) {
        // Log model details
        Log.d("loadImageIntoViewGroupOptimizedPdfGroup", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewGroupOptimizedPdfGroup", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewGroupOptimizedPdfGroup", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewGroupOptimizedPdfGroup", "Load High Quality: " + loadHighQuality);

        // Simple, fast PDF loading without complex blur transformations
        RequestBuilder<Drawable> glideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                ;

        // Single quality setting for all images - 75% quality
        glideRequest = glideRequest
                .override(400, 600) // PDF preview size
                .encodeQuality(75); // Single 75% quality for all images

        glideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Group PDF load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Group PDF loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }

    public static void loadImageIntoViewPdf(Context context, String imageSource, RequestOptions requestOptions,
                                            ImageView targetImageView, ViewGroup parentLayout, int position,
                                            boolean loadHighQuality, messageModel model) {

        Log.d("loadImageIntoViewPdf", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewPdf", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewPdf", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewPdf", "Load High Quality: " + loadHighQuality);

        // Strong Blur Transformation
        Transformation<Bitmap> blurTransformation = new BlurTransformation(4, 1); // ⬅️ Strong blur

        // Low-quality blurred thumbnail - optimized for scrolling
        RequestBuilder<Drawable> lowQualityPdfRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override(100, 100) // Force very low resolution
                .format(DecodeFormat.PREFER_RGB_565)
                .transform(blurTransformation) // ⬅️ Apply blur here
                .dontAnimate()
                ;

        // High-quality full load
        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                ;

        // Single quality setting for all images - 75% quality
        mainGlideRequest = mainGlideRequest
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityPdfRequest); // Use the blurred preview

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object modelObj, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object modelObj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }


    public static void loadImageIntoViewReplyImg(Context context, String imageSource, RequestOptions requestOptions,
                                            ImageView targetImageView, ViewGroup parentLayout, int position,
                                            boolean loadHighQuality, messageModel model) {

        Log.d("loadImageIntoViewPdf", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewPdf", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewPdf", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewPdf", "Load High Quality: " + loadHighQuality);

        // Strong Blur Transformation
        Transformation<Bitmap> blurTransformation = new BlurTransformation(4, 1); // ⬅️ Strong blur

        // Low-quality blurred thumbnail - optimized for scrolling
        RequestBuilder<Drawable> lowQualityPdfRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override(30, 30) // Even lower resolution to prevent black flashes
                .format(DecodeFormat.PREFER_RGB_565)
                .transform(blurTransformation) // Apply optimized blur
                .dontAnimate()
                .placeholder(android.R.color.transparent) // Transparent placeholder
                .error(android.R.color.transparent) // Transparent error
                .timeout(4000); // Shorter timeout

        // High-quality full load
        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                ;

        // Single quality setting for all images - 75% quality
        mainGlideRequest = mainGlideRequest
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityPdfRequest); // Use the blurred preview

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object modelObj, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object modelObj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }




    public static void loadImageIntoViewMic(Context context, String imageSource, RequestOptions requestOptions,
                                            ImageView targetImageView, ViewGroup parentLayout, int position,
                                            boolean loadHighQuality, messageModel model, ImageView videoicon) {

        Log.d("loadImageIntoViewPdf", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewPdf", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewPdf", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewPdf", "Load High Quality: " + loadHighQuality);

        // Strong Blur Transformation
        Transformation<Bitmap> blurTransformation = new BlurTransformation(4, 1); // ⬅️ Strong blur

        // Low-quality blurred thumbnail - optimized for scrolling
        RequestBuilder<Drawable> lowQualityPdfRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override(100, 100) // Force very low resolution
                .format(DecodeFormat.PREFER_RGB_565)
                .transform(blurTransformation) // ⬅️ Apply blur here
                .dontAnimate()
                ;

        // High-quality full load
        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                ;

        // Single quality setting for all images - 75% quality
        mainGlideRequest = mainGlideRequest
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityPdfRequest); // Use the blurred preview

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object modelObj, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object modelObj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }


    public static void loadImageIntoViewPdfGroup(Context context, String imageSource, RequestOptions requestOptions,
                                                 ImageView targetImageView, ViewGroup parentLayout, int position,
                                                 boolean loadHighQuality, group_messageModel model) { // Changed messageModel to Message

        // Log model details
        Log.d("loadImageIntoViewPdf", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewPdf", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewPdf", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewPdf", "Load High Quality: " + loadHighQuality);


        // Strong Blur Transformation
        Transformation<Bitmap> blurTransformation = new BlurTransformation(4, 1); // ⬅️ Strong blur

        // Low-quality blurred thumbnail
        RequestBuilder<Drawable> lowQualityPdfRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override(100, 100) // Force very low resolution
                .format(DecodeFormat.PREFER_RGB_565)
                .transform(blurTransformation) // ⬅️ Apply blur here
                .dontAnimate()
                ;


        // Build the main Glide request
        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)) // Ensure AUTOMATIC for both quality modes
                ; // TODO: Use a placeholder like R.drawable.pdf_placeholder or R.color.edittextBg


        // Single quality setting for all images - 75% quality
        mainGlideRequest = mainGlideRequest
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityPdfRequest); // Use the low-quality PDF request as a thumbnail

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object modelObj, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object modelObj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }


    public static void loadImageIntoViewGroupMic(Context context, String imageSource, RequestOptions requestOptions,
                                                 ImageView targetImageView, ViewGroup parentLayout, int position,
                                                 boolean loadHighQuality, group_messageModel model, ImageView videoicon) { // Changed messageModel to Message

        // Log model details
        Log.d("loadImageIntoViewPdf", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewPdf", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewPdf", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewPdf", "Load High Quality: " + loadHighQuality);


        // Strong Blur Transformation
        Transformation<Bitmap> blurTransformation = new BlurTransformation(4, 1); // ⬅️ Strong blur

        // Low-quality blurred thumbnail
        RequestBuilder<Drawable> lowQualityPdfRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override(100, 100) // Force very low resolution
                .format(DecodeFormat.PREFER_RGB_565)
                .transform(blurTransformation) // ⬅️ Apply blur here
                .dontAnimate()
                ;


        // Build the main Glide request
        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)) // Ensure AUTOMATIC for both quality modes
                ; // TODO: Use a placeholder like R.drawable.pdf_placeholder or R.color.edittextBg


        // Single quality setting for all images - 75% quality
        mainGlideRequest = mainGlideRequest
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityPdfRequest); // Use the low-quality PDF request as a thumbnail

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object modelObj, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Image load failed for: " + imageSource, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object modelObj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Image loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }
    public static void loadImageIntoViewGroup(Context context, String imageSource, RequestOptions requestOptions,
                                              ImageView targetImageView, ViewGroup parentLayout, int position,
                                              boolean loadHighQuality, group_messageModel model, View videoIcon) {

        float density = context.getResources().getDisplayMetrics().density;
        int orientation = context.getResources().getConfiguration().orientation;

        Log.d("loadImageIntoViewGroup", "getImageWidth: " + model.getImageWidth());
        Log.d("loadImageIntoViewGroup", "getImageHeight: " + model.getImageHeight());
        Log.d("loadImageIntoViewGroup", "getAspectRatio: " + model.getAspectRatio());
        Log.d("loadImageIntoViewGroup", "Orientation: " + (orientation == Configuration.ORIENTATION_PORTRAIT ? "Portrait" : "Landscape"));
        Log.d("loadImageIntoViewGroup", "Load High Quality: " + loadHighQuality);

        float imageWidthPx, imageHeightPx, aspectRatio;
        try {
            String width = model.getImageWidth();
            String height = model.getImageHeight();
            String ratio = model.getAspectRatio();

            imageWidthPx = Float.parseFloat((width != null && !width.trim().isEmpty()) ? width : "300");
            imageHeightPx = Float.parseFloat((height != null && !height.trim().isEmpty()) ? height : "300");
            aspectRatio = Float.parseFloat((ratio != null && !ratio.trim().isEmpty()) ? ratio : "1.0");
            if (aspectRatio <= 0) {
                aspectRatio = imageWidthPx / imageHeightPx;
            }
        } catch (NumberFormatException e) {
            Log.e("loadImageIntoViewGroup", "Invalid dimensions or aspect ratio, using defaults", e);
            imageWidthPx = 210f;
            imageHeightPx = 250f;
            aspectRatio = 1.0f;
        }

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
            finalWidthPx = (int) (maxHeightPx * aspectRatio);
            if (finalWidthPx > maxWidthPx) {
                finalWidthPx = maxWidthPx;
                finalHeightPx = (int) (maxWidthPx / aspectRatio);
            }
        }

        finalWidthPx = Math.min(finalWidthPx, maxWidthPx);
        finalHeightPx = Math.min(finalHeightPx, maxHeightPx);

        ViewGroup.LayoutParams params = targetImageView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(finalWidthPx, finalHeightPx);
        } else {
            params.width = finalWidthPx;
            params.height = finalHeightPx;
        }
        targetImageView.setLayoutParams(params);

        if (parentLayout != null) {
            ViewGroup.LayoutParams parentParams = parentLayout.getLayoutParams();
            if (parentParams != null) {
                parentParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                parentLayout.setLayoutParams(parentParams);
            }
        }

        // ✅ Apply strong blur transformation
        RequestBuilder<Drawable> lowQualityRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .override((int) (finalWidthPx * 0.2f), (int) (finalHeightPx * 0.2f))
                .transform(new BlurTransformation(4, 1)) // ✅ Blur applied
                .format(DecodeFormat.PREFER_ARGB_8888)    // ✅ Ensures quality for blur
                .dontAnimate()                            // ✅ OK
                ;                       // ✅ Optional


        RequestBuilder<Drawable> mainGlideRequest = Glide.with(context)
                .load(imageSource)
                .apply(requestOptions
                        .signature(new ObjectKey(position))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                ;

        // Single quality setting for all images - 75% quality
        videoIcon.setVisibility(View.VISIBLE);
        mainGlideRequest = mainGlideRequest
                .override(finalWidthPx, finalHeightPx)
                .encodeQuality(75) // Single 75% quality for all images
                .thumbnail(lowQualityRequest);

        mainGlideRequest
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Group Image/Video load failed for: " + imageSource, e);

                        // Check if this is a video decoding error
                        if (e != null && e.getCauses() != null) {
                            for (Throwable cause : e.getCauses()) {
                                if (cause instanceof RuntimeException &&
                                    cause.getMessage() != null &&
                                    cause.getMessage().contains("setDataSource failed")) {
                                    Log.w("Glide", "Group video thumbnail decoding failed, likely corrupted or unsupported format: " + imageSource);
                                    // Hide video icon for failed videos
                                    if (videoIcon != null) {
                                        videoIcon.setVisibility(View.VISIBLE);
                                    }
                                    // Try to load a fallback placeholder
                                    try {
                                        targetImageView.setImageResource(android.R.drawable.ic_media_play);
                                    } catch (Exception ignored) {
                                        // If even placeholder fails, just clear the image
                                        targetImageView.setImageDrawable(null);
                                    }
                                    return true; // Handled the error
                                }
                            }
                        }

                        // For other types of errors, hide video icon and try default handling
                        if (videoIcon != null) {
                            videoIcon.setVisibility(View.VISIBLE);
                        }
                        return false; // Let Glide handle other errors normally
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Group Image/Video loaded successfully: " + imageSource + " (High Quality: " + loadHighQuality + ")");
                        return false;
                    }
                })
                .into(targetImageView);
    }




    public static String[] calculateImageDimensions(Context context, File imageFile, Uri imageUri) {
        Log.d("CalculateImageDimensions", "=== STARTING DIMENSION CALCULATION ===");
        Log.d("CalculateImageDimensions", "imageFile: " + (imageFile != null ? imageFile.getAbsolutePath() : "null"));
        Log.d("CalculateImageDimensions", "imageUri: " + imageUri);
        Log.d("CalculateImageDimensions", "imageFile exists: " + (imageFile != null && imageFile.exists()));

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // फक्त डायमेंशन्स मिळवण्यासाठी

            // इमेज सोर्सवर आधारित डायमेंशन्स
            if (imageFile != null && imageFile.exists()) {
                Log.d("CalculateImageDimensions", "Using imageFile for dimensions");
                BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            } else if (imageUri != null) {
                Log.d("CalculateImageDimensions", "Using imageUri for dimensions");
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                BitmapFactory.decodeStream(inputStream, null, options);
                if (inputStream != null) {
                    inputStream.close();
                }
            } else {
                Log.e("CalculateImageDimensions", "Both imageFile and imageUri are null!");
            }

            int width = options.outWidth;
            int height = options.outHeight;

            Log.d("CalculateImageDimensions", "Raw dimensions - width: " + width + ", height: " + height);

            if (width <= 0 || height <= 0) {
                Log.e("CalculateImageDimensions", "Invalid dimensions detected! width=" + width + ", height=" + height);
                return new String[]{"300.00", "300.00", "1.00"};
            }

            // डिव्हाइस डेन्सिटी
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            Log.d("CalculateImageDimensions", "Device density: " + density);

            // dp मध्ये कन्व्हर्शन
            float imageWidthDp = width / density;
            float imageHeightDp = height / density;

            // आस्पेक्ट रेशिओ
            float aspectRatio = (float) width / height;

            // स्ट्रिंग्समध्ये कन्व्हर्शन
            String widthStr = String.format("%.2f", imageWidthDp);
            String heightStr = String.format("%.2f", imageHeightDp);
            String aspectRatioStr = String.format("%.2f", aspectRatio);

            // लॉगिंग (डीबगिंगसाठी)
            Log.d("CalculateImageDimensions", "Final calculated dimensions:");
            Log.d("CalculateImageDimensions", "  Width: " + widthStr + "dp");
            Log.d("CalculateImageDimensions", "  Height: " + heightStr + "dp");
            Log.d("CalculateImageDimensions", "  AspectRatio: " + aspectRatioStr);
            Log.d("CalculateImageDimensions", "=== DIMENSION CALCULATION COMPLETE ===");

            return new String[]{widthStr, heightStr, aspectRatioStr};
        } catch (Exception e) {
            Log.e("ImageDimensions", "Error calculating dimensions: " + e.getMessage());
            // डीफॉल्ट व्हॅल्यूज
            return new String[]{"300.00", "300.00", "1.00"};
        }
    }

    public static void dialogueFullScreen(Context mContext, int layout) {
        dialogLayoutFullScreen = new Dialog(mContext);
        ((android.app.Dialog) dialogLayoutFullScreen).setContentView(layout);
        dialogLayoutFullScreen.setCanceledOnTouchOutside(false);
        dialogLayoutFullScreen.setCancelable(false);
        dialogLayoutFullScreen.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLayoutFullScreen.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        Log.d("VIDEO_DIALOG", "dialogueFullScreen inflated layout: " + layout);

        // Handle back press to close dialog
        dialogLayoutFullScreen.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    dialog.dismiss();
                    if (playerPreview != null) {
                        playerPreview.stop();
                        playerPreview.release();
                    }

                    if (playerPreview2 != null) {
                        playerPreview2.stop();
                        playerPreview2.release();
                    }
                    return true;
                }
                return false;
            }
        });

        WindowManager.LayoutParams layoutParams = dialogLayoutFullScreen.getWindow().getAttributes();
        dialogLayoutFullScreen.getWindow().setAttributes(layoutParams);
        Window window = Constant.dialogLayoutFullScreen.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Log.d("VIDEO_DIALOG", "dialogueFullScreen window configured (MATCH_PARENT)");

        // dialogLayoutFullScreen.show(); // Don't forget to show the dialog!
    }

    public static String generateRoomIdFromTimestamp() {
        long timestamp = System.currentTimeMillis() / 1000; // current time in seconds
        return "EnclosurePowerfulNext" + timestamp;
    }

    /**
     * Simple image loader for basic cases like favicons, icons, etc.
     * @param context The context
     * @param imageSource The image source (URL, resource ID, etc.)
     * @param targetImageView The ImageView to load the image into
     * @param placeholderResId Placeholder resource ID
     * @param errorResId Error resource ID
     */
    public static void loadSimpleImage(Context context, String imageSource, ImageView targetImageView,
                                     int placeholderResId, int errorResId) {
        try {
            Glide.with(context)
                    .load(imageSource)
                    .placeholder(placeholderResId)
                    .error(errorResId)
                    .into(targetImageView);
        } catch (Exception e) {
            Log.e("loadSimpleImage", "Error loading image: " + e.getMessage());
            targetImageView.setImageResource(errorResId);
        }
    }

    /**
     * Simple image loader for resource IDs
     * @param context The context
     * @param resourceId The resource ID
     * @param targetImageView The ImageView to load the image into
     */
    public static void loadSimpleImage(Context context, int resourceId, ImageView targetImageView) {
        try {
            Glide.with(context)
                    .load(resourceId)
                    .into(targetImageView);
        } catch (Exception e) {
            Log.e("loadSimpleImage", "Error loading resource image: " + e.getMessage());
        }
    }

    /**
     * Get the appropriate Lottie animation resource for an emoji
     * @param emoji The emoji character
     * @return The Lottie animation resource ID, or -1 if no animation available
     */
    public static int getEmojiAnimationResource(String emoji) {
        // Map common emojis to their animation resources
        switch (emoji) {
            case "😀": case "😃": case "😄": case "😁": case "😆": case "😅": case "🤣": case "😂": case "🙂": case "🙃": case "😉": case "😊": case "😇":
                return R.raw.loader; // Use existing loader animation for happy faces
            case "❤️": case "💕": case "💖": case "💗": case "💘": case "💝": case "💞": case "💟":
                return R.raw.pending; // Use existing pending animation for hearts
            case "🎉": case "🎊": case "🎈": case "🎁":
                return R.raw.matching_animation; // Use existing matching animation for celebrations
            case "👍": case "👎": case "👌": case "✌️": case "🤞": case "🤟": case "🤘": case "🤙":
                return R.raw.radar; // Use existing radar animation for gestures
            default:
                return R.raw.loader; // Default animation for other emojis
        }
    }

    /**
     * Animate an emoji with a bounce effect
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void animateEmoji(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) {
            android.util.Log.d("EmojiAnimation", "animateEmoji: Invalid parameters - textView: " + textView + ", emoji: " + emoji);
            return;
        }

        android.util.Log.d("EmojiAnimation", "animateEmoji: Animating emoji: " + emoji);

        // Choose animation based on emoji type
        if (emoji.contains("❤️") || emoji.contains("💕") || emoji.contains("💖") || emoji.contains("💗") || emoji.contains("💘") || emoji.contains("💝") || emoji.contains("💞") || emoji.contains("💟")) {
            // Heart emojis get a pulse animation
            android.util.Log.d("EmojiAnimation", "animateEmoji: Using pulse animation for heart emoji");
            animateEmojiPulse(textView, emoji);
        } else if (emoji.contains("🎉") || emoji.contains("🎊") || emoji.contains("🎈") || emoji.contains("🎁")) {
            // Celebration emojis get a rotation animation
            android.util.Log.d("EmojiAnimation", "animateEmoji: Using rotation animation for celebration emoji");
            animateEmojiRotation(textView, emoji);
        } else if (emoji.contains("👍") || emoji.contains("👎") || emoji.contains("👌") || emoji.contains("✌️") || emoji.contains("🤞") || emoji.contains("🤟") || emoji.contains("🤘") || emoji.contains("🤙")) {
            // Gesture emojis get a bounce animation
            android.util.Log.d("EmojiAnimation", "animateEmoji: Using bounce animation for gesture emoji");
            animateEmojiBounce(textView, emoji);
        } else {
            // Default bounce animation for other emojis
            android.util.Log.d("EmojiAnimation", "animateEmoji: Using default bounce animation");
            animateEmojiBounce(textView, emoji);
        }
    }

    /**
     * Animate an emoji with a bounce effect
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void animateEmojiBounce(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) {
            android.util.Log.d("EmojiAnimation", "animateEmojiBounce: Invalid parameters");
            return;
        }

        android.util.Log.d("EmojiAnimation", "animateEmojiBounce: Starting bounce animation for: " + emoji);

        // Create a scale animation for the emoji
        textView.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction(() -> {
                    android.util.Log.d("EmojiAnimation", "animateEmojiBounce: Phase 1 complete, starting phase 2");
                    textView.animate()
                            .scaleX(0.9f)
                            .scaleY(0.9f)
                            .setDuration(100)
                            .withEndAction(() -> {
                                android.util.Log.d("EmojiAnimation", "animateEmojiBounce: Phase 2 complete, starting phase 3");
                                textView.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .withEndAction(() -> {
                                            android.util.Log.d("EmojiAnimation", "animateEmojiBounce: Animation complete");
                                        })
                                        .start();
                            })
                            .start();
                })
                .start();
    }

    /**
     * Animate an emoji with a rotation effect
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void animateEmojiRotation(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) return;

        // Create a rotation animation for the emoji
        textView.animate()
                .rotation(360f)
                .setDuration(500)
                .withEndAction(() -> {
                    textView.setRotation(0f);
                })
                .start();
    }

    /**
     * Animate an emoji with a pulse effect
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void animateEmojiPulse(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) return;

        // Create a pulse animation for the emoji
        textView.animate()
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction(() -> {
                    textView.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    /**
     * Create a delayed animation for emoji messages
     * This creates a more natural feel like Telegram
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     * @param delayMs Delay in milliseconds before starting animation
     */
    public static void animateEmojiDelayed(TextView textView, String emoji, long delayMs) {
        if (textView == null || emoji == null || emoji.isEmpty()) {
            android.util.Log.d("EmojiAnimation", "animateEmojiDelayed: Invalid parameters - textView: " + textView + ", emoji: " + emoji);
            return;
        }

        android.util.Log.d("EmojiAnimation", "animateEmojiDelayed: Starting animation for emoji: " + emoji + " with delay: " + delayMs + "ms");

        textView.postDelayed(() -> {
            android.util.Log.d("EmojiAnimation", "animateEmojiDelayed: Executing animation for emoji: " + emoji);
            animateEmoji(textView, emoji);
        }, delayMs);
    }

    /**
     * Create a continuous subtle animation for emoji messages
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void animateEmojiContinuous(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) return;

        // Create a subtle continuous animation
        textView.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(1000)
                .withEndAction(() -> {
                    textView.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(1000)
                            .withEndAction(() -> {
                                // Repeat the animation
                                animateEmojiContinuous(textView, emoji);
                            })
                            .start();
                })
                .start();
    }

    /**
     * Set up tap-to-animate functionality for emoji TextViews
     * Preserves existing long click listeners on parent views
     * @param textView The TextView containing the emoji
     * @param emoji The emoji character
     */
    public static void setupEmojiTapAnimation(TextView textView, String emoji) {
        if (textView == null || emoji == null || emoji.isEmpty()) return;

        // Use a custom touch listener that detects short taps without interfering with long press
        textView.setOnTouchListener(new View.OnTouchListener() {
            private long startTime;
            private boolean isLongPress = false;
            private Handler longPressHandler = new Handler();
            private Runnable longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    isLongPress = true;
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        isLongPress = false;
                        // Start a delayed runnable to detect long press
                        longPressHandler.postDelayed(longPressRunnable, 500); // 500ms threshold
                        return false; // Let parent handle the event

                    case MotionEvent.ACTION_UP:
                        // Cancel the long press detection
                        longPressHandler.removeCallbacks(longPressRunnable);

                        long duration = System.currentTimeMillis() - startTime;
                        if (duration < 500 && !isLongPress) { // Short tap (less than 500ms)
                            android.util.Log.d("EmojiAnimation", "Emoji tapped: " + emoji);
                            animateEmoji(textView, emoji);
                        }
                        return false; // Let parent handle the event

                    case MotionEvent.ACTION_CANCEL:
                        longPressHandler.removeCallbacks(longPressRunnable);
                        isLongPress = true;
                        return false; // Let parent handle the event
                }
                return false; // Always let parent handle the event
            }
        });
    }
}