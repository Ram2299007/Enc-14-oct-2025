package com.Appzia.enclosure.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Looper;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Appzia.enclosure.Adapter.chatAdapter;
import com.Appzia.enclosure.Adapter.childCallingLogAdapter;
import com.Appzia.enclosure.Utils.SwipeNavigationHelper;
import com.Appzia.enclosure.Adapter.childCallingLogAdapterVoice;
import com.Appzia.enclosure.Adapter.emojiAdapterChatAdapter;
import com.Appzia.enclosure.Adapter.emoji_adapter_addbtn;
import com.Appzia.enclosure.Adapter.get_user_active_chat_list_adapter;
import com.Appzia.enclosure.Adapter.groupChatAdapter;
import com.Appzia.enclosure.Adapter.grpListAdapter;
import com.Appzia.enclosure.Adapter.profilestatusAdapter;
import com.Appzia.enclosure.Adapter.unableDeliveredUserAdaptyer;
import com.Appzia.enclosure.Fragments.callFragment;
import com.Appzia.enclosure.Fragments.videoCallFragment;
import com.Appzia.enclosure.Fragments.youFragment;
import com.Appzia.enclosure.Model.BlockedUserModel;
import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.allContactListModel;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.change_numberModel;
import com.Appzia.enclosure.Model.contactGetModel;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.flagNewModel;
import com.Appzia.enclosure.Model.flagNewModelChild;
import com.Appzia.enclosure.Model.forwardnameModel;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.Model.globalModel;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.grp_list_child_model;
import com.Appzia.enclosure.Model.grp_list_model;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.Appzia.enclosure.models.members;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.Model.unableDeliveredUserMdoel;
import com.Appzia.enclosure.R;
import com.Appzia.enclosure.Screens.MainActivityOld;
import com.Appzia.enclosure.Screens.SplashScreenMy;
import com.Appzia.enclosure.Screens.ViewProfile;
import com.Appzia.enclosure.Screens.block_contact_activity;
import com.Appzia.enclosure.Screens.chattingScreen;
import com.Appzia.enclosure.Screens.editmyProfile;
import com.Appzia.enclosure.Screens.flagScreen;
import com.Appzia.enclosure.Screens.forGroupVisible;
import com.Appzia.enclosure.Screens.forgetScreenOtp;
import com.Appzia.enclosure.Screens.grpChattingScreen;
import com.Appzia.enclosure.Screens.inviteScreen;
import com.Appzia.enclosure.Screens.lockScreen2;
import com.Appzia.enclosure.Screens.lockscreen;
import com.Appzia.enclosure.Screens.newGroupActivity;
import com.Appzia.enclosure.Screens.otpverifyScreen;
import com.Appzia.enclosure.Screens.otpverifyScreenDelete;
import com.Appzia.enclosure.Screens.privacy_policy;
import com.Appzia.enclosure.Screens.shareExternalDataCONTACTScreen;
import com.Appzia.enclosure.Screens.show_image_Screen;
import com.Appzia.enclosure.Screens.userInfoScreen;

import com.Appzia.enclosure.Screens.whatsTheCode;
import com.Appzia.enclosure.Screens.whatsYourNumber;
import com.Appzia.enclosure.SubScreens.CallUtil;
import com.Appzia.enclosure.SubScreens.ChattingRoomUtils;
import com.Appzia.enclosure.SubScreens.GroupMsgUtils;
import com.Appzia.enclosure.SubScreens.MsgLimitUtils;
import com.Appzia.enclosure.SubScreens.VideoCallUtil;
import com.Appzia.enclosure.SubScreens.YouUtils;
import com.Appzia.enclosure.Utils.OfflineDatabase.DatabaseHelper;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.API;
import com.Appzia.enclosure.Utils.WebserviceRetrofits.APIClient;
import com.Appzia.enclosure.activities.CallActivity;
import com.Appzia.enclosure.models.get_call_log_1Child;
import com.Appzia.enclosure.models.get_call_log_1Model;
import com.Appzia.enclosure.models.get_group_detailsResponseModel;
import com.Appzia.enclosure.models.groupD;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Webservice {


    public static long downloadId;
    public static final String TAG = "Webservice";
    public static final String BASE_URL = "https://confidential.enclosureapp.com/";
    private static final String SEND_OTP = BASE_URL + "send_otp";
    private static final String GET_BLOCKED_USERS = BASE_URL + "get_blocked_users";
    private static final String GET_BLOCK_COUNT = BASE_URL + "get_block_count";
    private static AsyncHttpClient client;
    private static long lastNotificationTime = 0L;
    private static String lastCallTime = null;
    private static String lastCallType = null;
    private static int lastStatus = -1;
    private static final long NOTIFICATION_DEBOUNCE_MS = 500;
    private static final OkHttpClient clientXX = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    // todo this is can use for send otp for other page
    private static final String SEND_OTP_COMMON = BASE_URL + "send_otp_common";
    // todo this is can use for verify otp for other page
    private static final String VERIFY_OTP_FOR_CHANGE_NUMBER = BASE_URL + "verify_otp_for_change_number";
    private static final String INSERT_OR_UPDATE_CONTACT = BASE_URL + "insert_or_update_contact";
    private static final String VERIFY_MOBILE_OTP = BASE_URL + "verify_mobile_otp";
    private static final String CHECK_PHONE_ID_MATCH = BASE_URL + "check_phone_id_match";
    private static final String CREATE_NAME = BASE_URL + "create_name";
    private static final String GET_USER_ACTIVE_CHAT_LIST = BASE_URL + "get_user_active_chat_list";
    private static final String DELETE_INDIVIDUAL_USER_CHATTING = BASE_URL + "delete_individual_user_chatting";
    private static final String CREATE_GROUP_FOR_CHATTING = BASE_URL + "create_group_for_chatting";
    private static final String GET_GRP_LIST = BASE_URL + "get_group_list";
    private static final String GETTING_CALLING_CONTACT_LIST = BASE_URL + "get_calling_contact_list";
    private static final String GET_CALL_LOG = BASE_URL + "get_call_log_1";
    private static final String GET_VOICE_CALL_LOG = BASE_URL + "get_voice_call_log";
    private static final String CLEAR_VOICE_CALL_LIST = BASE_URL + "clear_voice_calling_list";
    private static final String CLEAR_VIDEO_CALLING_LIST = BASE_URL + "clear_video_calling_list";
    private static final String CREATE_GROUP_CALLLING = BASE_URL + "create_group_calling";
    private static final String CREATE_VOICE_CALLING = BASE_URL + "create_voice_calling";
    private static final String SET_MSG_LIMIT = BASE_URL + "set_message_limit_for_user_chat";
    private static final String SET_MSG_LIMIT_FOR_ALL = BASE_URL + "set_message_limit_for_all_users";
    private static final String GET_MESSAGE_LIMT_FOR = BASE_URL + "get_message_limit_for_all_users";
    private static final String SEARCH_IN_MSGLMT = BASE_URL + "search_user_by_name_or_number";
    private static final String SEARCH_USER_IN_VOICE_AND_VIDEO_CALL = BASE_URL + "search_user_for_voice_and_video_call";
    //private static final String GET_INDIVIDUAL_ID = BASE_URL + "get_individual_chatting";
    private static final String GET_INDIVIDUAL_ID = BASE_URL + "reset_notification_count";
    // private static final String GET_INDIVIDUAL_ID = BASE_URL + "get_individual_chattingcawbjckwabbcaw";
    private static final String CREATE_INDIVIDUAL_CHATTING = BASE_URL + "create_individual_chatting";
    private static final String CREATE_GROUP_CHATTING = BASE_URL + "create_group_chatting";
    private static final String GET_USER_ACTIVE_CONTACT_LIST = BASE_URL + "get_user_active_contact_list";
    private static final String DELETE_USER_SINGLE_STATUS_IMAGE = BASE_URL + "delete_user_single_status_image";
    private static final String GET_USER_CONTACT_LIST = BASE_URL + "get_user_contact_list";
    private static final String GET_USERS_ALL_CONTACT = BASE_URL + "get_users_all_contact";
    private static final String search_from_all_contact = BASE_URL + "search_from_all_contact";
    private static final String UPLOAD_USER_CONTACT_LIST = BASE_URL + "upload_user_contact_list";
    private static final String GET_PRIVACY_POLICY = BASE_URL + "get_privacy_policy";
    private static final String GET_IN_TOUCH = BASE_URL + "get_in_touch";
    private static final String GET_USER_PROFILE_IMAGES = BASE_URL + "get_user_profile_images";
    private static final String BLOCK_USER = BASE_URL + "block_user";
    private static final String UNBLOCK_USER = BASE_URL + "unblock_user";
    private static final String IS_USER_BLOCKED_URL = BASE_URL + "is_user_blocked";
    private static final String GET_USER_PROFILE_IMAGES_100 = BASE_URL + "get_user_profile_images_100";
    private static final String UPLOAD_USER_PROFILE_IMAGES = BASE_URL + "upload_user_profile_images";
    private static final String DELETE_USER_PROFILE = BASE_URL + "delete_user_profile_image";
    private static final String GET_PROFILE = BASE_URL + "get_profile";
    private static final String GET_PROFILE_100 = BASE_URL + "get_profile_100";
    private static final String PROFILE_UPDATE = BASE_URL + "profile_update";
    private static final String LOCK_SCREEN = BASE_URL + "lock_screen";
    private static final String FORGET_LOCK_SCREEN = BASE_URL + "forget_lock_screen";

    // Utility method to safely load images with Picasso
    private static void safeLoadImage(String photoUrl, ImageView imageView, int placeholderResId) {
        try {
            if (photoUrl != null && !photoUrl.trim().isEmpty()) {
                Picasso.get().load(photoUrl).placeholder(placeholderResId).into(imageView);
            } else {
                // Load placeholder if photo URL is null or empty
                Picasso.get().load(placeholderResId).placeholder(placeholderResId).into(imageView);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            // Fallback to placeholder on any error
            Picasso.get().load(placeholderResId).placeholder(placeholderResId).into(imageView);
        }
    }

    public static void send_otp(final Context mContext, String mobile_no, String country_Code, String aNull, whatsYourNumber whatsYourNumber, ProgressBar progressBar, String c_id) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobile_no);
            request_param.put("c_id", c_id);
            // Toast.makeText(mContext, c_id, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "@@@send_otp :" + SEND_OTP + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");

                                    JSONArray array = response.getJSONArray("data");

                                    JSONObject obj = array.getJSONObject(0);

                                    String uid = obj.getString("uid");

//                                    String mob_otp = obj.getString("mob_otp");
//                                    Log.d("mob_otp#", mob_otp);

                                    progressBar.setVisibility(View.GONE);

                                    if (aNull.equals("null")) {
                                        // Toast.makeText(mContext, mob_otp, Toast.LENGTH_SHORT).show();

                                    } else {

                                        if (mContext != null) {
                                            Intent intent = new Intent(mContext, whatsTheCode.class);
                                            intent.putExtra("uidKey", uid);
                                            intent.putExtra("c_id", c_id);
                                            intent.putExtra("phoneKy", mobile_no);
                                            intent.putExtra("country_codeKey", country_Code);
                                            SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                            //


                                        }
                                    }


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void send_otp2(final Context mContext, String mobile_no, String country_Code, String aNull, String cId) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobile_no);
            request_param.put("c_id", cId);

            Log.d(TAG, "@@@send_otp :" + SEND_OTP + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");

                                    JSONArray array = response.getJSONArray("data");

                                    JSONObject obj = array.getJSONObject(0);

                                    String uid = obj.getString("uid");
                                    //     String mob_otp = obj.getString("mob_otp");
                                    //   Log.d("mob_otp#", mob_otp);


                                    if (aNull.equals("null")) {
                                        // Toast.makeText(mContext, mob_otp, Toast.LENGTH_SHORT).show();

                                    } else {

                                        if (mContext != null) {
                                            Intent intent = new Intent(mContext, whatsTheCode.class);
                                            intent.putExtra("uidKey", uid);

                                            intent.putExtra("phoneKy", mobile_no);
                                            intent.putExtra("country_codeKey", country_Code);
                                            SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                            //


                                        }
                                    }


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void verify_mobile_otp(final Context mContext, String uid, String mob_otp, String country_codeKey, String token, String deviceId, ProgressBar progressBar, File file, String fileName) {
        try {
            String phoneId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("phoneId", phoneId);
            client = new AsyncHttpClient();
            client.setConnectTimeout(30000);
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("mob_otp", mob_otp);
            request_param.put("f_token", token);
            request_param.put("device_id", deviceId);
            request_param.put("phone_id", phoneId);
           // With this:
            if (token != null) {
            Log.d("f_token", token);
            } else {
            Log.d("f_token", "token is null");
            }
            Log.d(TAG, "@@@verify_mobile_otp :" + VERIFY_MOBILE_OTP + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(VERIFY_MOBILE_OTP, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@verify_mobile_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    // here need to add progress]
                                    ProgressDialog dialog;
                                    dialog = new ProgressDialog(mContext);
                                    dialog.setTitle("Please wait");
                                    dialog.setMessage("Your contacts are synchronizing...");
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();

                                    String message = response.getString("message");

                                    JSONArray array = response.getJSONArray("data");
                                    JSONObject obj = array.getJSONObject(0);
                                    String phone = obj.getString("mobile_no");
                                    String f_token = obj.getString("f_token");


                                    if (mContext != null) {
                                        Constant.setSfFunction(mContext);
                                        Constant.setSF.putString(Constant.PHONE_NUMBERKEY, phone);
                                        Constant.setSF.putString(Constant.UID_KEY, uid);
                                        Constant.setSF.putString(Constant.FCM_TOKEN, f_token);
                                        Constant.setSF.apply();
                                        Webservice.upload_user_contact_listRetrofitFIRST(mContext, uid, file, dialog, fileName, country_codeKey);
                                    }


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            Log.d(TAG, "crashed area: " + "Yes");
            e.printStackTrace();
        }
    }


    public static void check_phone_id_match(final Context mContext, String mobile_no, PhoneIdCheckListener listener) {
        try {
            String phone_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("phoneId", phone_id);

            AsyncHttpClient client = new AsyncHttpClient(); // Create fresh client instance
            RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobile_no);
            request_param.put("phone_id", phone_id);

            String finalUrl = "https://confidential.enclosureapp.com/check_phone_id_match";
            Log.d("Webservice", "check_phone_id_match URL: " + finalUrl + "?" + request_param.toString());

            client.get(finalUrl, request_param, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("Webservice", "check_phone_id_match response: " + response.toString());
                    try {
                        int status = response.getInt("error_code");
                        if (status == 200) {
                            listener.onSuccess(response); // ✅ Match found
                        } else if (status == 409) {
                            listener.onPartialMatch(response); // ⚠️ Phone ID mismatch
                        } else {
                            String message = response.optString("message", "Unknown error");
                            listener.onFailure(message); // ❌ 404 or 406 etc.
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure("Invalid JSON response");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("Webservice", "Failure (String): " + responseString, throwable);
                    listener.onFailure("Request failed (String): " + responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Webservice", "Failure (JSON): " + errorResponse, throwable);
                    listener.onFailure("Request failed (JSON): " + (errorResponse != null ? errorResponse.toString() : throwable.getMessage()));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.e("Webservice", "Failure (Array): " + errorResponse, throwable);
                    listener.onFailure("Request failed (Array): " + throwable.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Webservice", "Exception in check_phone_id_match", e);
            listener.onFailure("Exception: " + e.getMessage());
        }
    }


    public static void getPhoneIdByMobile(Context context, String mobileNo, PhoneIdFetchListener listener) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("mobile_no", mobileNo);

            String url = "https://confidential.enclosureapp.com/get_phone_id_by_mobile";
            Log.d("Webservice", "Request URL: " + url + "?" + params.toString());

            client.get(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("Webservice", "Response: " + response.toString());
                    try {
                        int code = response.getInt("error_code");
                        if (code == 200) {
                            listener.onSuccess(response.getJSONObject("data"));
                        } else {
                            listener.onFailure(response.optString("message", "Unknown error"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure("Invalid JSON response");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("Webservice", "Failure (String): " + responseString, throwable);
                    listener.onFailure("Request failed: " + responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Webservice", "Failure (JSON): " + errorResponse, throwable);
                    listener.onFailure("Request failed: " + throwable.getMessage());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.e("Webservice", "Failure (Array): " + errorResponse, throwable);
                    listener.onFailure("Request failed: " + throwable.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Webservice", "Exception: ", e);
            listener.onFailure("Exception: " + e.getMessage());
        }
    }

    public static void insert_or_update_contact(Context mContext, String uid, String myOwnNumber, String contactName, String contactNumber, chattingScreen chattingScreen, TextView textViewname) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid);
            requestParams.put("mobile_no", myOwnNumber);
            requestParams.put("contact_name", contactName);
            requestParams.put("contact_number", contactNumber);

            Log.d("InsertContact", "Sending request to: " + INSERT_OR_UPDATE_CONTACT + "?" + requestParams.toString());

            ((Activity) mContext).runOnUiThread(() -> {
                client.post(INSERT_OR_UPDATE_CONTACT, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("InsertContact", "Response: " + response.toString());

                        try {
                            String status = response.optString("status");
                            String message = response.optString("message");

                            if ("inserted".equalsIgnoreCase(status) || "updated".equalsIgnoreCase(status)) {
                                // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                chattingScreen.setupRecyclerViewAndAdapter2();
                                textViewname.setText(contactName);
                            } else {
                                // Toast.makeText(mContext, "Unexpected response: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("InsertContact", "Error parsing response: " + e.getMessage(), e);
                            //Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("InsertContact", "onFailure(String): Status " + statusCode + " | Response: " + responseString, throwable);
                        //  Toast.makeText(mContext, "Failed to connect to server (code: " + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("InsertContact", "onFailure(JSONObject): Status " + statusCode + " | Error: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                        // Toast.makeText(mContext, "Server error (code: " + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("InsertContact", "onFailure(JSONArray): Status " + statusCode + " | Error: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                        //   Toast.makeText(mContext, "Server error (code: " + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } catch (Exception e) {
            Log.e("InsertContact", "Exception: " + e.getMessage(), e);
            //Toast.makeText(mContext, "Unexpected exception occurred", Toast.LENGTH_SHORT).show();
        }
    }

    public static void insertBlockUser(Context context, String uid, String blockedUid, Dialog dialog, chattingScreen chattingScreen, TextView blockUser, LinearLayout blockContainer, LinearLayout messageboxContainer) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid);                // The user who is blocking
            requestParams.put("blocked_uid", blockedUid); // The user being blocked
            Log.d("InsertBlockUser", "Sending request to: " + BLOCK_USER + "?" + requestParams.toString());

            ((Activity) context).runOnUiThread(() -> {
                client.post(BLOCK_USER, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("InsertBlockUser", "Response: " + response.toString());

                        String status = response.optString("status");
                        String message = response.optString("message");

                        if ("success".equalsIgnoreCase(status)) {
                            //Toast.makeText(context, "User blocked successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            blockUser.setText("true");
                            chattingScreen.setupRecyclerViewAndAdapter2();
                            blockContainer.setVisibility(View.VISIBLE);
                            messageboxContainer.setVisibility(View.GONE);

                        } else {
                            // Toast.makeText(context, "Failed to block: " + message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            blockUser.setText("true");
                            chattingScreen.setupRecyclerViewAndAdapter2();
                            blockContainer.setVisibility(View.VISIBLE);
                            messageboxContainer.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("InsertBlockUser", "onFailure: " + responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("InsertBlockUser", "onFailure JSONObject: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("InsertBlockUser", "onFailure JSONArray: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }
                });
            });

        } catch (Exception e) {
            Log.e("InsertBlockUser", "Exception: " + e.getMessage(), e);
        }
    }

    public static void getBlockedUsers(Context context, String uid, block_contact_activity blockContactActivity, ProgressBar progressBar) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid); // The user who blocked others

            Log.d("GetBlockedUsers", "Sending request to: " + GET_BLOCKED_USERS + "?" + requestParams.toString());

            ((Activity) context).runOnUiThread(() -> {
                client.post(GET_BLOCKED_USERS, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("GetBlockedUsers", "Response: " + response.toString());

                        String status = response.optString("status");
                        int blockCount = response.optInt("block_count", 0);

                        ArrayList<BlockedUserModel> blockedUserList = new ArrayList<>();

                        if ("success".equalsIgnoreCase(status)) {
                            JSONArray blockedUsersArray = response.optJSONArray("blocked_users");
                            if (blockedUsersArray != null) {
                                for (int i = 0; i < blockedUsersArray.length(); i++) {
                                    JSONObject user = blockedUsersArray.optJSONObject(i);
                                    if (user != null) {
                                        String blockedUid = user.optString("uid");
                                        String fullName = user.optString("full_name");
                                        String photo = user.optString("photo");

                                        Log.d("BlockedUser", "UID: " + blockedUid + ", Name: " + fullName + ", Photo: " + photo);

                                        // TODO: Handle or display the user data in your UI here

                                        blockedUserList.add(new BlockedUserModel(blockedUid, fullName, photo));
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                blockContactActivity.setAdapter(blockedUserList);
                            }

                            // Optionally, show total count
                           // Toast.makeText(context, "Blocked count: " + blockCount, Toast.LENGTH_SHORT).show();
                        } else {
                           // Toast.makeText(context, "No blocked users found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("GetBlockedUsers", "onFailure: " + responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("GetBlockedUsers", "onFailure JSONObject: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("GetBlockedUsers", "onFailure JSONArray: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }
                });
            });

        } catch (Exception e) {
            Log.e("GetBlockedUsers", "Exception: " + e.getMessage(), e);
        }
    }

    public static void getBlockCount(Context context, String uid, TextView blockCountTextView) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid); // The user whose block count we want

            Log.d("GetBlockCount", "Sending request to: " + GET_BLOCK_COUNT + "?" + requestParams.toString());

            ((Activity) context).runOnUiThread(() -> {
                client.post(GET_BLOCK_COUNT, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("GetBlockCount", "Response: " + response.toString());

                        String status = response.optString("status");
                        int count = response.optInt("block_count", 0);

                        if ("success".equalsIgnoreCase(status)) {
                            // Show count in the UI
                            blockCountTextView.setText(String.valueOf(count));
                            // You can also use it elsewhere if needed
                        } else {
                            blockCountTextView.setText("0");
                          //  Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("GetBlockCount", "onFailure: " + responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("GetBlockCount", "onFailure JSONObject: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("GetBlockCount", "onFailure JSONArray: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }
                });
            });

        } catch (Exception e) {
            Log.e("GetBlockCount", "Exception: " + e.getMessage(), e);
        }
    }


    public static void unblockUser(Context context, String uid, String blockedUid, chattingScreen chattingScreen, LinearLayout blockContainer, LinearLayout messageboxContainer, TextView blockUser) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid);                // The user who is unblocking
            requestParams.put("blocked_uid", blockedUid); // The user being unblocked

            Log.d("UnblockUser", "Sending request to: " + UNBLOCK_USER + "?" + requestParams.toString());

            ((Activity) context).runOnUiThread(() -> {
                client.post(UNBLOCK_USER, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("UnblockUser", "Response: " + response.toString());

                        String status = response.optString("status");
                        String message = response.optString("message");

                        if ("success".equalsIgnoreCase(status)) {
                            //Toast.makeText(context, "User unblocked successfully", Toast.LENGTH_SHORT).show();

                            blockUser.setText("false");
                            chattingScreen.setupRecyclerViewAndAdapter();
                            blockContainer.setVisibility(View.GONE);
                            messageboxContainer.setVisibility(View.VISIBLE);
                        } else {
                            //Toast.makeText(context, "Failed to unblock: " + message, Toast.LENGTH_SHORT).show();

                            blockUser.setText("false");
                            chattingScreen.setupRecyclerViewAndAdapter();
                            blockContainer.setVisibility(View.GONE);
                            messageboxContainer.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("UnblockUser", "onFailure: " + responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("UnblockUser", "onFailure JSONObject: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("UnblockUser", "onFailure JSONArray: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }
                });
            });

        } catch (Exception e) {
            Log.e("UnblockUser", "Exception: " + e.getMessage(), e);
        }
    }


    public static void unblockUserSetting(Context context, String uid, String blockedUid) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid);                // The user who is unblocking
            requestParams.put("blocked_uid", blockedUid); // The user being unblocked

            Log.d("UnblockUser", "Sendingrequesttoffffffff: " + UNBLOCK_USER + "?" + requestParams.toString());

            ((Activity) context).runOnUiThread(() -> {
                client.post(UNBLOCK_USER, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("UnblockUser", "Response: " + response.toString());

                        String status = response.optString("status");
                        String message = response.optString("message");

                        if ("success".equalsIgnoreCase(status)) {

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("UnblockUser", "onFailure: " + responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("UnblockUser", "onFailure JSONObject: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("UnblockUser", "onFailure JSONArray: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                    }
                });
            });

        } catch (Exception e) {
            Log.e("UnblockUser", "Exception: " + e.getMessage(), e);
        }
    }


    public static void checkIfBlockedByReceiver(Context context, String uid, String receiverId, CallbackBlock callback) {
        try {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams requestParams = new RequestParams();
            requestParams.put("uid", uid);               // You
            requestParams.put("receiver_id", receiverId); // Receiver

           // Log.d("CheckBlockStatus", "Sending request to: " + IS_USER_BLOCKED_URL + "?" + requestParams.toString());

            client.post(IS_USER_BLOCKED_URL, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              //      Log.d("CheckBlockStatus", "Response (" + statusCode + "): " + response.toString());

                    if (statusCode == 200) {
                        // ✅ You are blocked
                        callback.onBlocked(true, response.optString("message"));
                    } else {
                        // Other 2xx response (fallback)
                        callback.onBlocked(false, "Unexpected response code: " + statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (statusCode == 401) {
                        // ❌ You are not blocked
                        callback.onBlocked(false, "You are not blocked");
                    } else if (statusCode == 400) {
                        callback.onError("Missing parameters");
                    } else {
                        callback.onError("Unknown error (" + statusCode + ")");
                    }

                   // Log.e("CheckBlockStatus", "onFailure: " + (errorResponse != null ? errorResponse.toString() : "null"), throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    callback.onError("Network error (" + statusCode + "): " + responseString);
                  //  Log.e("CheckBlockStatus", "onFailure: " + responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    callback.onError("Unexpected failure (" + statusCode + ")");
                  //  Log.e("CheckBlockStatus", "onFailure JSONArray: " + errorResponse, throwable);
                }
            });

        } catch (Exception e) {
            callback.onError("Exception: " + e.getMessage());
          //  Log.e("CheckBlockStatus", "Exception: " + e.getMessage(), e);
        }
    }

    public interface CallbackBlock {
        void onBlocked(boolean isBlocked, String message);

        void onError(String error);
    }


    public interface PhoneIdFetchListener {
        void onSuccess(JSONObject data);

        void onFailure(String errorMessage);
    }


    public interface PhoneIdCheckListener {
        void onSuccess(JSONObject response);         // when both match

        void onPartialMatch(JSONObject response);    // mobile found but phone_id doesn't match

        void onFailure(String error);                // all other failures
    }


    public static void verify_mobile_otpCHANGENUMBER(final Context mContext, String uid, String mob_otp, String country_codeKey, String token, String deviceId, String phoneKy) {
        try {

            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("otp", mob_otp);
            Log.d("f_token", token);
            Log.d(TAG, "@@@verify_mobile_otp :" + VERIFY_OTP_FOR_CHANGE_NUMBER + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(VERIFY_OTP_FOR_CHANGE_NUMBER, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@verify_mobile_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                                    if (mContext != null) {
                                        Constant.setSfFunction(mContext);
                                        Constant.setSF.putString(Constant.PHONE_NUMBERKEY, phoneKy);
                                        Constant.setSF.putString(Constant.UID_KEY, uid);
                                        Constant.setSF.apply();

                                        Intent intent = new Intent(mContext, lockScreen2.class);
                                        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                        //

                                        SwipeNavigationHelper.finishWithSwipe((Activity) mContext);

                                        Constant.setSfFunction(mContext);
                                        Constant.setSF.putString(Constant.loggedInKey, Constant.loggedInKey);
                                        Log.d("country_codeKey", country_codeKey);
                                        Constant.setSF.putString(Constant.COUNTRY_CODE, country_codeKey);
                                        Constant.setSF.apply();
                                    }


                                } else {
                                    String message = response.getString("message");
                                  //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void create_name(final Context mContext, String uid, String full_name) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("full_name", full_name);

            Log.d(TAG, "@@@create_name :" + CREATE_NAME + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_NAME, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    MainActivityOld.dialogLayoutColor.dismiss();


                                    Constant.setSfFunction(mContext);
                                    Constant.setSF.putString("nameSAved", "nameSAved");
                                    Constant.setSF.putString(Constant.full_name, full_name);
                                    Constant.setSF.apply();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void lock_screen(final Context mContext, String uid, String lock_screen, String lock_screen_pin, String lock3, CardView customToastCard, TextView customToastText) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("lock_screen", lock_screen);
            request_param.put("lock_screen_pin", lock_screen_pin);

            Log.d("pin", uid + lock_screen + lock_screen_pin);

            Log.d(TAG, "@@@lock_screen :" + LOCK_SCREEN + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(LOCK_SCREEN, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@lock_screen_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");


                                    if (lock_screen.equals("1")) {

                                        Constant.setSfFunction(mContext);
                                        Constant.getSfFuncion(mContext);

                                        Constant.setSF.putString("lockKey", lock_screen_pin);
                                        Constant.setSF.putString("sleepKeyCheckOFF", "");
                                        Log.d("password", lock_screen_pin);
                                        Constant.setSF.apply();


                                        if (lock_screen_pin.equals("360")) {
                                            if (Constant.getSF.getString(Constant.sleepKey, "").equals(Constant.sleepKey)) {

                                                Constant.showCustomToast("Sleep Mode - ON", customToastCard, customToastText);
                                            } else {
                                                Constant.showCustomToast("Password set in degree", customToastCard, customToastText);

                                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, new Intent(mContext, MainActivityOld.class));
                                                    }
                                                }, 1000);


                                            }

                                        } else {
                                            Constant.showCustomToast("Password set in degree", customToastCard, customToastText);

                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, new Intent(mContext, MainActivityOld.class));
                                                }
                                            }, 1000);

                                            //

                                        }
                                    } else if (lock_screen.equals("0")) {

                                        if (lock3.equals(Constant.lock3)) {
                                            SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, new Intent(mContext, lockscreen.class));
                                            //

                                        } else {

                                            if (message.equals("Screen unlocked !")) {
                                                Constant.getSfFuncion(mContext);
                                                String sleepkey = Constant.getSF.getString(Constant.sleepKey, "");

                                                if (sleepkey.equals(Constant.sleepKey)) {
                                                    //after unlock notification will wotk done
                                                    Constant.setSfFunction(mContext);
                                                    //   Constant.setSF.putString("lockKey", "0");
                                                    Constant.setSF.putString(Constant.sleepKey, "");
                                                    Constant.setSF.putString("sleepKeyCheckOFF", "sleepKeyCheckOFF");
                                                    Constant.setSF.apply();
                                                    // Toast.makeText(mContext, "done", Toast.LENGTH_SHORT).show();
                                                }

                                                Intent intent = new Intent(mContext, MainActivityOld.class);
                                                intent.putExtra("lockSuccess", "lockSuccess");
                                                intent.putExtra("sleepKeyCheckOFF", "sleepKeyCheckOFF");
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                                //    Toast.makeText(mContext, "Sleep Mode - OFF", Toast.LENGTH_SHORT).show();


                                            } else {

                                                Constant.showCustomToast(message, customToastCard, customToastText);
                                            }
                                        }
                                    }


                                } else {
                                    String message = response.getString("message");

                                    Constant.showCustomToast(message, customToastCard, customToastText);
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void lock_screenDummy(final Context mContext, String uid, String lock_screen, String lock_screen_pin, String lock3, CardView customToastCard, TextView customToastText) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("lock_screen", lock_screen);
            request_param.put("lock_screen_pin", lock_screen_pin);

            Log.d("pin", uid + lock_screen + lock_screen_pin);

            Log.d(TAG, "@@@lock_screen :" + LOCK_SCREEN + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(LOCK_SCREEN, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@lock_screen_response :" + response.toString());


                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void forget_lock_screen(Context mContext, String uid, String mobilenumber) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("mobile_no", mobilenumber);
            Log.d("pin", uid + mobilenumber);

            Log.d(TAG, "@@@forget_lock_screen :" + FORGET_LOCK_SCREEN + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(FORGET_LOCK_SCREEN, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@forget_lock_screen_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    JSONObject obj = array.getJSONObject(0);
                                    String phone = obj.getString("mobile_no");
                                    String lock_pin = obj.getString("lock_screen_pin");
                                    Toast.makeText(mContext, "Your password is : " + lock_pin, Toast.LENGTH_SHORT).show();


                                    ((Activity) mContext).onBackPressed();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }

    }

    public static void profile_update(Context mContext, String uid, String name, String caption, File imageFile, CardView customToastCard, TextView customToastText) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("full_name", name);
            request_param.put("caption", caption);
            if (imageFile != null) {

                request_param.put("photo", imageFile);
            }

            Log.d(TAG, "@@@profile_update :" + PROFILE_UPDATE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(PROFILE_UPDATE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@profile_update_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    if (imageFile != null) {
                                        imageFile.delete();
                                    }

                                    String message = response.getString("message");
                                    //    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    Constant.showCustomToast("Profile changed", customToastCard, customToastText);
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Activity) mContext).onBackPressed();
                                        }
                                    }, 1000); // 1000 milliseconds = 1 second


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_profile_UserInfo(Context mContext, String uid) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");

                                    try {
                                        new DatabaseHelper(mContext).insert_get_profileTable(mContext, uid, full_name, caption, mobile_no, photo, f_token);

                                    } catch (Exception e) {
                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                    }


                                    safeLoadImage(photo, userInfoScreen.profile, R.drawable.inviteimg);
                                    userInfoScreen.pName.setText(full_name);
                                    userInfoScreen.pCaption.setText(caption);
                                    userInfoScreen.phone.setText(mobile_no);
                                    userInfoScreen.profile.setTag(photo);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_profile_UserInfo_Under(Context mContext, String uid, ImageView profile, TextView pName, TextView pCaption, TextView phone, String viewer_uid, String f_token) {

        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("viewer_uid", viewer_uid);
            request_param.put("f_token", f_token);
            Log.d(TAG, "@@@create_name :" + GET_PROFILE_100 + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GET_PROFILE_100, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_responsecccccc :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");

                                    try {
                                        new DatabaseHelper(mContext).insert_get_profileTable(mContext, uid, full_name, caption, mobile_no, photo, f_token);

                                    } catch (Exception e) {
                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                    }


                                    safeLoadImage(photo, profile, R.drawable.inviteimg);
                                    pName.setText(full_name);
                                    pCaption.setText(caption);
                                    phone.setText(mobile_no);
                                    profile.setTag(photo);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_UserInfoEmoji(Context mContext, String uid, TextView contact1text, ImageView contact1img) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");

                                    contact1text.setText(full_name);
                                    safeLoadImage(photo, contact1img, R.drawable.inviteimg);

                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_ViewProfile(Context mContext, String uid) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");

                                    //     Toast.makeText(mContext, full_name, Toast.LENGTH_SHORT).show();

                                    safeLoadImage(photo, ViewProfile.profile, R.drawable.inviteimg);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_MainActivity(Context mContext, String uid) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");

                                    //     Toast.makeText(mContext, full_name, Toast.LENGTH_SHORT).show();


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_YouFragment(Context mContext, String uid, ProgressBar progressBar) {

        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");


                                    try {
                                        new DatabaseHelper(mContext).insert_get_profileTable(mContext, uid, full_name, caption, mobile_no, photo, f_token);

                                    } catch (Exception e) {
                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                    }

                                    safeLoadImage(photo, youFragment.profile, R.drawable.inviteimg);
                                    youFragment.name.setText(full_name);

                                    youFragment.caption.setText(caption);


                                    youFragment.phone.setText(mobile_no);

                                    youFragment.profile.setTag(photo);

                                    youFragment.youImg.setVisibility(View.VISIBLE);


                                    youFragment.multiimageRecyclerview.setVisibility(View.VISIBLE);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }

                                    progressBar.setVisibility(View.GONE);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_YouFragment2(Context mContext, String uid, ProgressBar progressBar, TextView name, TextView captions, TextView phone, ImageView profile, LinearLayout youImg, RecyclerView multiimageRecyclerview) {

        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");


                                    try {
                                        new DatabaseHelper(mContext).insert_get_profileTable(mContext, uid, full_name, caption, mobile_no, photo, f_token);

                                    } catch (Exception e) {
                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                    }

                                    safeLoadImage(photo, profile, R.drawable.inviteimg);
                                    name.setText(full_name);

                                    captions.setText(caption);


                                    phone.setText(mobile_no);

                                    profile.setTag(photo);

                                    youImg.setVisibility(View.VISIBLE);


                                    multiimageRecyclerview.setVisibility(View.VISIBLE);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }

                                    progressBar.setVisibility(View.GONE);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_profile_EditProfile(Context mContext, String uid) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@create_name :" + GET_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_name_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {


                                    JSONArray arr = response.getJSONArray("data");
                                    JSONObject obj = arr.getJSONObject(0);
                                    String full_name = obj.getString("full_name");
                                    String caption = obj.getString("caption");
                                    String mobile_no = obj.getString("mobile_no");
                                    String photo = obj.getString("photo");
                                    String f_token = obj.getString("f_token");


                                    try {
                                        new DatabaseHelper(mContext).insert_get_profileTable(mContext, uid, full_name, caption, mobile_no, photo, f_token);

                                    } catch (Exception e) {
                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                    }


                                    safeLoadImage(photo, editmyProfile.profile, R.drawable.inviteimg);
                                    //   Toast.makeText(mContext, full_name, Toast.LENGTH_SHORT).show();
                                    editmyProfile.pName.setText(full_name);
                                    editmyProfile.pCaption.setText(caption);
                                    editmyProfile.profile.setTag(photo);


                                    editmyProfile.profile.setVisibility(View.VISIBLE);


                                    try {
                                        if (photo != null) {
                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.profilePic, photo);
                                            Constant.setSF.putString(Constant.full_name, full_name);
                                            Constant.setSF.apply();
                                        }

                                    } catch (Exception ignored) {

                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void delete_user_profile_image(Context mContext, String uid) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "delete_user_profile_image: " + uid);


            Log.d(TAG, "@@@delete_user_profile_image :" + DELETE_USER_PROFILE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(DELETE_USER_PROFILE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_user_profile_image_response :" + response.toString());

                            try {
                                String error_code = response.getString("error_code");
                                if (error_code.equals("200")) {
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    editmyProfile.selectedImageUri = null;
                                    Picasso.get().load(R.drawable.inviteimg).into(editmyProfile.profile);

                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void upload_user_profile_images(Context mContext, String uid, File imageFile2, editmyProfile editmyProfile, ProgressBar progressBar) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            if (imageFile2 != null) {
                request_param.put("photo", imageFile2);
            }

            Log.d(TAG, "@@@upload_user_profile_images :" + UPLOAD_USER_PROFILE_IMAGES + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(UPLOAD_USER_PROFILE_IMAGES, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@upload_user_profile_images_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    //       Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    JSONArray array = response.getJSONArray("data");
                                    JSONObject obj = array.getJSONObject(0);
                                    String id = obj.getString("id");

                                    Webservice.get_user_profile_images(mContext, uid, editmyProfile, progressBar);


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_user_profile_images_youFragment(Context mContext, String uid, YouUtils youFragment, LinearLayout linearLayout) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_profile_images :" + GET_USER_PROFILE_IMAGES + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_PROFILE_IMAGES, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_profile_images_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Constant.profilestatusList.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String photo = obj.getString("photo");
                                        String id = obj.getString("id");

                                        Log.d(TAG, "photo :my" + photo);
                                        //   Toast.makeText(mContext,"my"+ message, Toast.LENGTH_SHORT).show();

                                        Constant.profilestatusList.add(new profilestatusModel(photo, id));


                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_profile_imagesTable(mContext, uid, id, photo, "f_token");

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                    }
                                    linearLayout.setVisibility(View.VISIBLE);


                                    youFragment.setAdapter(Constant.profilestatusList);


                                } else {
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_user_profile_images_userInfo(Context mContext, String uid, userInfoScreen userInfoScreen, ImageView profile, TextView pName, TextView pCaption, TextView phone, String viewer_uid, String f_token) {

        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("viewer_uid", viewer_uid);
            request_param.put("f_token", f_token);


            Log.d(TAG, "@@@get_user_profile_images :" + GET_USER_PROFILE_IMAGES_100 + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_PROFILE_IMAGES_100, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_profile_images_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    Constant.profilestatusList.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String photo = obj.getString("photo");
                                        String id = obj.getString("id");

                                        Log.d(TAG, "photo :XXXX" + photo);


                                        Constant.profilestatusList.add(new profilestatusModel(photo, id));

                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_profile_imagesTable(mContext, uid, id, photo, f_token);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }
                                    }

                                    userInfoScreen.setAdapter(Constant.profilestatusList);


                                } else {
                                    String message = response.getString("message");
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_user_profile_images(Context mContext, String uid, editmyProfile editmyProfile, ProgressBar profile) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_profile_images :" + GET_USER_PROFILE_IMAGES + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_PROFILE_IMAGES, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_profile_images_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    JSONArray array = response.getJSONArray("data");
                                    String photo = null;
                                    int i = 0;

                                    Constant.profilestatusList.clear();
                                    for (i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        photo = obj.getString("photo");
                                        String id = obj.getString("id");


                                        Constant.profilestatusList.add(new profilestatusModel(photo, id));


                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_profile_imagesTable(mContext, uid, id, photo, "f_token");

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }
                                    }

                                    if (photo != null) {
                                        if (i == 1) {

                                            com.Appzia.enclosure.Screens.editmyProfile.four.setVisibility(View.GONE);


                                        } else if (i == 2) {

                                            com.Appzia.enclosure.Screens.editmyProfile.four.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.three.setVisibility(View.GONE);

                                        } else if (i == 3) {

                                            com.Appzia.enclosure.Screens.editmyProfile.four.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.three.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.two.setVisibility(View.GONE);

                                        } else if (i == 4) {

                                            com.Appzia.enclosure.Screens.editmyProfile.four.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.three.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.two.setVisibility(View.GONE);
                                            com.Appzia.enclosure.Screens.editmyProfile.one.setVisibility(View.GONE);
                                        } else {

                                            com.Appzia.enclosure.Screens.editmyProfile.four.setVisibility(View.VISIBLE);
                                            com.Appzia.enclosure.Screens.editmyProfile.three.setVisibility(View.VISIBLE);
                                            com.Appzia.enclosure.Screens.editmyProfile.two.setVisibility(View.VISIBLE);
                                            com.Appzia.enclosure.Screens.editmyProfile.one.setVisibility(View.VISIBLE);
                                        }
                                    }


                                    com.Appzia.enclosure.Screens.editmyProfile.topper.setVisibility(View.VISIBLE);
                                    com.Appzia.enclosure.Screens.editmyProfile.profile.setVisibility(View.VISIBLE);
                                    editmyProfile.setAdapter(Constant.profilestatusList);
                                    profile.setVisibility(View.GONE);


                                } else {
                                    profile.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_in_touch(Context mContext, String uid, String name, String email, String msg) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("full_name", name);
            request_param.put("email", email);
            request_param.put("message", msg);

            Log.d(TAG, "@@@get_in_touch :" + GET_IN_TOUCH + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_IN_TOUCH, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_in_touch_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    Toast.makeText(mContext, "Your message is received.", Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_privacy_policy(Context mContext, String uid, WebView webView) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_privacy_policy :" + GET_PRIVACY_POLICY + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_PRIVACY_POLICY, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_privacy_policy_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    JSONObject obj = array.getJSONObject(0);
                                    String privacy = obj.getString("privacy_policy");

                                    privacy_policy.ol.setText(privacy);


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

//    public static void upload_user_contact_list(Context mContext, String uid, String user_contacts, String mob_otpKey, String country_codeKey, String token, String deviceId) {
//        try {
//
//            client = new AsyncHttpClient();
//            client.setConnectTimeout(300000); // 30 seconds
//            client.setResponseTimeout(300000);
//            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
//            final RequestParams request_param = new RequestParams();
//            request_param.put("uid", uid);
//            request_param.put("user_contacts", user_contacts);
//
//            Log.d("test&&", uid);
//            Log.d("test&&", user_contacts);
//
//
//            Log.d(TAG, "@@@upload_user_contact_list :" + UPLOAD_USER_CONTACT_LIST + "?" + request_param.toString());
//            ((Activity) mContext).runOnUiThread(new Runnable() {
//                public void run() {
//
//                    client.post(UPLOAD_USER_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            Log.d(TAG, "@@@upload_user_contact_list_response :" + response.toString());
//
//                            try {
//                                int status = response.getInt("error_code");
//                                if (status == 200) {
//
//
//                                    String message = response.getString("message");
//                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                                    whatsTheCode.dialog.dismiss();
//
//                                    Webservice.verify_mobile_otp(mContext, uid, mob_otpKey, country_codeKey, token, deviceId);
//
//
//                                } else {
//                                    String message = response.getString("message");
//                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//
//                                e.printStackTrace();
//
//                                Log.d("@@@ notSuccess: ", e.getMessage());
//                            }
//                            super.onSuccess(statusCode, headers, response);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//                            //
//                            // Log.d(TAG, "onFailure: " + responseString);
//                            super.onFailure(statusCode, headers, responseString, throwable);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//
////
//                            //   Log.d(TAG, "onFailure: " + errorResponse.toString());
//
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//
//                            //
//                            //    Log.d(TAG, "onFailure: " + errorResponse.toString());
//
//
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//                    });
//                }
//            });
//        } catch (Exception e) {
//
//            //
//
//
//            e.printStackTrace();
//        }
//    }


    public static void get_user_contact_list(Context mContext, String uid, inviteScreen inviteScreene) {
        try {


            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_contact_list :" + GET_USER_CONTACT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GET_USER_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_contact_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Log.d("length", String.valueOf(array.length()));
                                    Constant.contactGetList.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String contact_name = obj.getString("contact_name");
                                        String contact_number = obj.getString("contact_number");
                                        Log.d("contact_name", contact_name + " " + contact_number);

                                        Constant.contactGetList.add(new contactGetModel(contact_name, contact_number));

                                    }


                                    inviteScreen.linear.setVisibility(View.VISIBLE);
                                    // inviteScreene.setAdapter(Constant.contactGetList);
                                    //    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete_user_single_status_image(Context mContext, String uid, String id, int adapterPosition, Dialog dialogLayoutColor, profilestatusAdapter profilestatusAdapter) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("id", id);

            Log.d("uidid", uid + " " + id);


            Log.d(TAG, "@@@delete_user_single_status_image :" + DELETE_USER_SINGLE_STATUS_IMAGE + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(DELETE_USER_SINGLE_STATUS_IMAGE, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_user_single_status_image_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    try {

                                        profilestatusAdapter.removeItem(adapterPosition);
                                        dialogLayoutColor.dismiss();
                                    } catch (Exception ex) {

                                    }

                                } else {
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.

                                    onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

//    public static void get_user_active_contact_list(Context mContext, String uid, inviteScreen inviteScreen) {
//
//
//        try {
//            client = new AsyncHttpClient();
//            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
//            final RequestParams request_param = new RequestParams();
//            request_param.put("uid", uid);
//            Log.d(TAG, "@@@get_user_active_contact_list :" + GET_USER_ACTIVE_CONTACT_LIST + "?" + request_param.toString());
//            ((Activity) mContext).runOnUiThread(new Runnable() {
//                public void run() {
//
//                    client.post(GET_USER_ACTIVE_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            Log.d(TAG, "@@@get_user_active_contact_list_response :" + response.toString());
//
//                            try {
//                                int status = response.getInt("error_code");
//                                if (status == 200) {
//
//                                    String message = response.getString("message");
//                                    //  Toast.makeText(mContext, "my"+message, Toast.LENGTH_SHORT).show();
//                                    JSONArray array = response.getJSONArray("data");
//                                    Constant.get_user_active_contact_list.clear();
//                                    for (int i = 0; i < array.length(); i++) {
//                                        JSONObject obj = array.getJSONObject(i);
//                                        String photo = obj.getString("photo");
//                                        String full_name = obj.getString("full_name");
//                                        String mobile_no = obj.getString("mobile_no");
//                                        String caption = obj.getString("caption");
//                                        String msg_limit = obj.getString("msg_limit");
//                                        String f_token = obj.getString("f_token");
//                                        String uid = obj.getString("uid");
//                                        String device_type = obj.getString("device_type");
//                                        String room = obj.getString("room");
//
//                                        String sent_time = null;
//                                        String dataType = null;
//                                        String messages = null;
//                                        try {
//                                            sent_time = obj.getString("sent_time");
//                                            dataType = obj.getString("dataType");
//                                            messages = obj.getString("message");
//                                        } catch (Exception ignored) {
//                                        }
//
//                                        Log.d("caption$my", caption);
//                                        Constant.get_user_active_contact_list.add(new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, 0, msg_limit, device_type, room));
//
//                                    }
//                                    //   inviteScreen.setCurrentUserAdapter(Constant.get_user_active_contact_list);
//
//
//                                } else {
//                                    String message = response.getString("message");
//                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//
//                                e.printStackTrace();
//
//                                Log.d("@@@ notSuccess: ", e.getMessage());
//                            }
//                            super.onSuccess(statusCode, headers, response);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//
//                            super.onFailure(statusCode, headers, responseString, throwable);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//
//                            //
//
//
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//
//                            //
//
//
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//                    });
//                }
//            });
//        } catch (Exception e) {
//
//
//            e.printStackTrace();
//        }
//    }

    public static void create_individual_chatting(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, chattingScreen personalmsgLimitMsg, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, ImageView cancel, chatAdapter chatAdapters, FirebaseDatabase database, String userFTokenKey, Activity mActivity, int notification, ArrayList<messageModel> messageList, File savedThumbnail, RecyclerView messageRecView, String deviceType, TextView limitStatus, TextView totalMsgLimita, String fTokenKey) {
        try {
            Log.d(TAG, "create_individual_chatting: " + messages);
            SecretKey key;
            int count = 0;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
            Constant.setSF.apply();
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            try {
                request_param.put("uid", senderId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("friend_id", receiverUid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("message", messages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("user_name", user_name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            request_param.put("notification", String.valueOf(1));
            if (upload_docs == null) {
                request_param.put("upload_docs", "");
            } else {

                try {
                    request_param.put("upload_docs", upload_docs);
                } catch (Exception ex) {

                }
            }
            try {
                request_param.put("dataType", dataType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("dataType", dataType);
            try {
                request_param.put("extension", extension);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("name", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("phone", phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("micPhoto", profilepic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("miceTiming", miceTiming);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("sent_time", sent_time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("model_id", model.getModelId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("fTokenKey", fTokenKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Log.d("idddd", senderId + ":" + receiverUid + ":" + messages);
            Log.d(TAG, "@@@create_individual_chatting :" + CREATE_INDIVIDUAL_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_individual_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    JSONObject data = response.getJSONObject("data");
                                    String total_msg_limit = data.getString("total_msg_limit");
                                    String user_name = data.getString("user_name");

                                    if (!total_msg_limit.equals("0")) {

                                        totalMsgLimita.setText(total_msg_limit);

                                    } else {
                                        database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {


                                                try {
                                                    // for sender data
                                                    chatAdapters.checkBarIsActive = 1;
                                                } catch (Exception ignored) {
                                                }
                                                database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        // todo remove last item
                                                        try {
                                                            chatAdapters.setLastItemVisible(false);
                                                        } catch (Exception e) {

                                                        }


                                                        // for notify download
                                                        mContext.registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);
                                                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                                                        }


                                                        if (userFTokenKey != null) {


                                                            Constant.getSfFuncion(mContext);
                                                            String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                                                            if (sleepKey.equals(Constant.sleepKey)) {

                                                            } else {

                                                                Log.d(TAG, "cabcjkqbkca: " + model.getDataType());

                                                                String fcm = "";
                                                                try {
                                                                    if (model.getDataType().equals(Constant.Text)) {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), model.getMessage(), senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else if (model.getDataType().equals(Constant.img)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have a new Image", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(),

                                                                                model.getSelectionCount());

                                                                    } else if (model.getDataType().equals(Constant.contact)) {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have a new Contact", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(),

                                                                                model.getSelectionCount());


                                                                    } else if (model.getDataType().equals(Constant.voiceAudio)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a Audio", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else if (model.getDataType().equals(Constant.video)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a Video", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a File", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());
                                                                    }
                                                                } catch (Exception e) {
                                                                    //  throw new RuntimeException(e);
                                                                }
                                                            }


                                                        }


                                                        String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();

                                                        database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {


                                                                try {


                                                                    // todo   Secured key
                                                                    //  YBM7kODvEs6B5GydYTZI3w==

                                                                } catch (Exception e) {
                                                                    throw new RuntimeException(e);
                                                                }


                                                            }
                                                        });


                                                    }
                                                });
                                            }
                                        });


                                    }


                                } else if (status == 403) {

                                    Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void create_individual_chattingForward(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, chattingScreen personalmsgLimitMsg, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, FirebaseDatabase database, String userFTokenKey, Activity mActivity, String deviceType, BottomSheetDialog bottomSheetDialog, int i, ArrayList<forwardnameModel> forwardNameList, ProgressBar progressBarMainNew) {

        try {
            progressBarMainNew.setVisibility(View.VISIBLE);
            Log.d("Forward", "Sending message: " + messages);

            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
            Constant.setSF.apply();

            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            RequestParams request_param = new RequestParams();

            request_param.put("uid", senderId);
            request_param.put("friend_id", receiverUid);
            request_param.put("message", messages);
            request_param.put("user_name", user_name);
            request_param.put("notification", "1");

            request_param.put("upload_docs", upload_docs != null ? upload_docs : "");
            request_param.put("dataType", dataType);
            request_param.put("extension", extension);
            request_param.put("name", name);
            request_param.put("phone", phone);
            request_param.put("micPhoto", profilepic);
            request_param.put("miceTiming", miceTiming);
            request_param.put("sent_time", sent_time);
            request_param.put("model_id", model.getModelId());

            Log.d("Forward", "Params: " + request_param.toString());

            ((Activity) mContext).runOnUiThread(() -> {
                client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            int status = response.getInt("error_code");
                            if (status == 200) {
                                JSONObject data = response.getJSONObject("data");
                                int limit_status = data.optInt("limit_status", 0);

                                if (limit_status == 1) {
                                    // Not eligible to send
                                    String total_msg_limit = data.optString("total_msg_limit", "0");
                                    Toast.makeText(mContext, "Message limit reached: " + total_msg_limit, Toast.LENGTH_SHORT).show();
                                } else {
                                    database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).setValue(model).addOnSuccessListener(unused -> database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).setValue(model).addOnSuccessListener(unused1 -> {
                                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                                        }

                                        mContext.registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);

                                        // Send FCM
                                        if (userFTokenKey != null && !Constant.getSF.getString(Constant.sleepKey, "").equals(Constant.sleepKey)) {
                                            String fcmMsg = getFCMMessage(model.getDataType(), model);
                                            Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), fcmMsg, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(),

                                                    model.getSelectionCount());
                                        }

                                        // Update socket
                                        String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();
                                        database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey);


                                        Constant.getSfFuncion(mContext);
                                        //  Webservice.get_individual_chattingUnion(mContext,Constant.getSF.getString(Constant.UID_KEY,""),receiverUid);


                                        // Final navigation after last forward
                                        if (i == forwardNameList.size() - 1) {
                                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                Intent intent = new Intent(mContext, MainActivityOld.class);
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                                progressBarMainNew.setVisibility(View.GONE);
                                                bottomSheetDialog.dismiss();
                                            }, 1000); // 1000 milliseconds = 1 second delay
                                        }

                                    }));
                                }
                            } else {
                                //  Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Forward", "JSONException: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("Forward", "Failure: " + throwable.getMessage());
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Forward", "Failure (JSON): " + throwable.getMessage());
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("Forward", "Failure (Array): " + throwable.getMessage());
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFCMMessage(String dataType, messageModel model) {
        if (dataType.equals(Constant.img)) {
            return "You have a new Image";
        } else if (dataType.equals(Constant.contact)) {
            return "You have a new Contact";
        } else if (dataType.equals(Constant.voiceAudio)) {
            return "You have a new Audio";
        } else if (dataType.equals(Constant.video)) {
            return "You have a new Video";
        } else {
            return dataType.equals(Constant.Text) ? model.getMessage() : "You have a new File";
        }
    }


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void end_notification_api(Context mContext, String userFTokenKey, String userName, String message,
                                            String senderId, String userName1, String profile, String sentTime,
                                            String deviceType, String uid, String message1, String time, String document,
                                            String dataType, String extension, String name, String phone, String micPhoto,
                                            String miceTiming, String userName2, String replytextData, String replyKey,
                                            String replyType, String replyOldData, String replyCrtPostion, String modelId,
                                            String receiverUid, String forwardedKey, String groupName, String docSize,
                                            String fileName, String thumbnail, String fileNameThumbnail, String caption,
                                            int notification, String currentDate, String selectionCount) {

        Log.d("PowerDataXXX2025", "uidPower: " + uid);
        Log.d("PowerDataXXX2025", "messagePower: " + message);
        Log.d("PowerDataXXX2025", "timePower: " + time);
        Log.d("PowerDataXXX2025", "documentPower: " + document);
        Log.d("PowerDataXXX2025", "dataTypePower: " + dataType);
        Log.d("PowerDataXXX2025", "extensionPower: " + extension);
        Log.d("PowerDataXXX2025", "namepower: " + name);
        Log.d("PowerDataXXX2025", "phonePower: " + phone);
        Log.d("PowerDataXXX2025", "micPhotoPower: " + micPhoto);
        Log.d("PowerDataXXX2025", "miceTimingPower: " + miceTiming);
        Log.d("PowerDataXXX2025", "userNamePower: " + userName);
        Log.d("PowerDataXXX2025", "replytextDataPower: " + replytextData);
        Log.d("PowerDataXXX2025", "replyKeyPower: " + replyKey);
        Log.d("PowerDataXXX2025", "replyTypePower: " + replyType);
        Log.d("PowerDataXXX2025", "replyOldDataPower: " + replyOldData);
        Log.d("PowerDataXXX2025", "replyCrtPostionPower: " + replyCrtPostion);
        Log.d("PowerDataXXX2025", "modelIdPower: " + modelId);
        Log.d("PowerDataXXX2025", "receiverUidPower: " + receiverUid);
        Log.d("PowerDataXXX2025", "groupNamePower: " + groupName);
        Log.d("PowerDataXXX2025", "docSizePower: " + docSize);
        Log.d("PowerDataXXX2025", "fileNamePower: " + fileName);
        Log.d("PowerDataXXX2025", "thumbnailPower: " + thumbnail);
        Log.d("PowerDataXXX2025", "fileNameThumbnailPower: " + fileNameThumbnail);
        Log.d("PowerDataXXX2025", "captionPower: " + caption);
        Log.d("PowerDataXXX2025", "notificationPower: " + notification);
        Log.d("PowerDataXXX2025", "currentDatePower: " + currentDate);
        Log.d("PowerDataXXX2025", "senderId: " + senderId);
        Log.d("PowerDataXXX2025", "userFTokenKey: " + userFTokenKey);
        Log.d("PowerDataXXX2025", "selectionCount: " + selectionCount);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastNotificationTime < NOTIFICATION_DEBOUNCE_MS) {
            Log.d(TAG, "Debouncing notification for modelId: " + modelId);
            return;
        }
        lastNotificationTime = currentTime;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String truncatedMessage = truncateToWords(message, 100);
                Log.d(TAG, "Preparing notification for modelId: " + modelId + ", uid: " + uid + ", receiverUid: " + receiverUid);

                Accesstoken accessToken = new Accesstoken();
                String accessTokenKey = accessToken.getAccessToke();

                if (accessTokenKey == null || accessTokenKey.isEmpty()) {
                    Log.e(TAG, "Invalid access token for modelId: " + modelId);
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(mContext, "Failed to send notification: Invalid access token", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }


                Constant.getSfFuncion(mContext);
              String   userFTokenKey2 = Constant.getSF.getString(Constant.FCM_TOKEN,"");

                JSONObject requestJson = new JSONObject();
                requestJson.put("deviceToken", safeString(userFTokenKey));
                requestJson.put("myFcmOwn", safeString(userFTokenKey2));
                requestJson.put("accessToken", safeString(accessTokenKey));
                requestJson.put("title", safeString(userName));
                requestJson.put("body", safeString(truncatedMessage));
                requestJson.put("receiverKey", safeString(senderId));
                requestJson.put("user_name", safeString(userName1));
                requestJson.put("photo", safeString(profile));
                requestJson.put("currentDateTimeString", safeString(sentTime));
                requestJson.put("deviceType", safeString(deviceType));
                requestJson.put("bodyKey", Constant.chatting);
                requestJson.put("click_action", "OPEN_ACTIVITY_1");
                requestJson.put("icon", "notification_icon");
                requestJson.put("modelId", safeString(modelId));
                requestJson.put("receiverUid", safeString(receiverUid));
                requestJson.put("forwardedKey", safeString(forwardedKey));
                requestJson.put("dataType", safeString(dataType));
                requestJson.put("selectionCount", safeString(selectionCount));

                Log.d(TAG, "Notification request JSON for modelId: " + modelId + ": " + requestJson.toString());

                RequestBody body = RequestBody.create(JSON, requestJson.toString());
                Request request = new Request.Builder()
                        .url(BASE_URL + "EmojiController/send_notification_api")
                        .post(body)
                        .build();

                Log.d(TAG, "Sending notification to URL: " + request.url() + " for modelId: " + modelId);

                clientXX.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Log.e(TAG, "Notification failed for modelId: " + modelId + ": " + e.getMessage(), e);
                        handleError(mContext, modelId, e);
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                        String responseBody = response.body().string(); // ✅ Corrected from .toString() to .string()
                        Log.d(TAG, "Notification response for modelId: " + modelId + ", Status: " + response.code() + ", Body: " + responseBody);
                        if (!response.isSuccessful()) {
                            handleError(mContext, modelId, new IOException("HTTP " + response.code() + ": " + responseBody));
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Notification error for modelId: " + modelId + ": " + e.getMessage(), e);
                handleError(mContext, modelId, e);
            }
        });
    }


    private static String safeString(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "NA";
    }


    private static void handleError(Context context, String modelId, Throwable throwable) {
        String errorMessage;
        if (throwable instanceof java.net.SocketTimeoutException) {
            errorMessage = "Notification timed out for modelId: " + modelId + ". Check network.";
        } else {
            errorMessage = "Notification failed for modelId: " + modelId + ": " + throwable.getMessage();
        }
        Log.e(TAG, errorMessage);
        //  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private static void end_notification_apiReply(Context mContext, String userFTokenKey, String userName, String message, String senderId, String userName1, String profile, String sentTime, String deviceType, String uid, String message1, String time, String document, String dataType, String extension, String name, String phone, String micPhoto, String miceTiming, String userName2, String replytextData, String replyKey, String replyType, String replyOldData, String replyCrtPostion, String modelId, String receiverUid, String forwaredKey, String groupName, String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption, int notification, String currentDate, String senderTokenReplyPower) {
        try {


            String truncatedMessage = truncateToWordss(message, 100);
            Log.d("PowerDataXXX2025", "uidPower: " + uid);
            Log.d("PowerDataXXX2025", "messagePower: " + message);
            Log.d("PowerDataXXX2025", "timePower: " + time);
            Log.d("PowerDataXXX2025", "documentPower: " + document);
            Log.d("PowerDataXXX2025", "dataTypePower: " + dataType);
            Log.d("PowerDataXXX2025", "extensionPower: " + extension);
            Log.d("PowerDataXXX2025", "namepower: " + name);
            Log.d("PowerDataXXX2025", "phonePower: " + phone);
            Log.d("PowerDataXXX2025", "micPhotoPower: " + micPhoto);
            Log.d("PowerDataXXX2025", "miceTimingPower: " + miceTiming);
            Log.d("PowerDataXXX2025", "userNamePower: " + userName);
            Log.d("PowerDataXXX2025", "replytextDataPower: " + replytextData);
            Log.d("PowerDataXXX2025", "replyKeyPower: " + replyKey);
            Log.d("PowerDataXXX2025", "replyTypePower: " + replyType);
            Log.d("PowerDataXXX2025", "replyOldDataPower: " + replyOldData);
            Log.d("PowerDataXXX2025", "replyCrtPostionPower: " + replyCrtPostion);
            Log.d("PowerDataXXX2025", "modelIdPower: " + modelId);
            Log.d("PowerDataXXX2025", "receiverUidPower: " + receiverUid);
            Log.d("PowerDataXXX2025", "forwaredKeyPower: " + forwaredKey);
            Log.d("PowerDataXXX2025", "groupNamePower: " + groupName);
            Log.d("PowerDataXXX2025", "docSizePower: " + docSize);
            Log.d("PowerDataXXX2025", "fileNamePower: " + fileName);
            Log.d("PowerDataXXX2025", "thumbnailPower: " + thumbnail);
            Log.d("PowerDataXXX2025", "fileNameThumbnailPower: " + fileNameThumbnail);
            Log.d("PowerDataXXX2025", "captionPower: " + caption);
            Log.d("PowerDataXXX2025", "notificationPower: " + notification);
            Log.d("PowerDataXXX2025", "currentDatePower: " + currentDate);
            Log.d("PowerDataXXX2025", "userFTokenKeyPower: " + userFTokenKey);
            Log.d("PowerDataXXX2025", "fcm: " + senderTokenReplyPower);

            Accesstoken accessToken = new Accesstoken();
            String accessTokenKey = accessToken.getAccessToke();
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            JSONObject requestJson = new JSONObject();

            requestJson.put("deviceToken", userFTokenKey);
            requestJson.put("accessToken", accessTokenKey);
            requestJson.put("accessTokenKey", accessTokenKey);
            requestJson.put("userFcmToken", senderTokenReplyPower);
            requestJson.put("senderTokenReply", senderTokenReplyPower);
            requestJson.put("title", String.valueOf(userName));
            requestJson.put("body", String.valueOf(truncatedMessage));
            requestJson.put("receiverKey", String.valueOf(senderId));
            requestJson.put("user_name", String.valueOf(userName1));
            requestJson.put("photo", String.valueOf(profile));
            requestJson.put("currentDateTimeString", String.valueOf(sentTime));
            requestJson.put("deviceType", String.valueOf(deviceType));
            requestJson.put("bodyKey", Constant.chatting);
            requestJson.put("click_action", "OPEN_ACTIVITY_1");
            requestJson.put("icon", "notification_icon");
            requestJson.put("uid", String.valueOf(uid));
            requestJson.put("message", String.valueOf(truncatedMessage));
            requestJson.put("time", String.valueOf(time));
            requestJson.put("document", String.valueOf(document));
            requestJson.put("dataType", String.valueOf(dataType));
            requestJson.put("extension", String.valueOf(extension));
            requestJson.put("name", String.valueOf(name));
            requestJson.put("phone", String.valueOf(phone));
            requestJson.put("miceTiming", String.valueOf(miceTiming));
            requestJson.put("micPhoto", String.valueOf(micPhoto));
            requestJson.put("userName", String.valueOf(userName2));
            requestJson.put("replytextData", String.valueOf(replytextData));
            requestJson.put("replyKey", String.valueOf(replyKey));
            requestJson.put("replyType", String.valueOf(replyType));
            requestJson.put("replyOldData", String.valueOf(replyOldData));
            requestJson.put("replyCrtPostion", String.valueOf(replyCrtPostion));
            requestJson.put("modelId", String.valueOf(modelId));
            requestJson.put("receiverUid", String.valueOf(receiverUid));
            requestJson.put("forwaredKey", String.valueOf(forwaredKey));
            requestJson.put("groupName", String.valueOf(groupName));
            requestJson.put("docSize", String.valueOf(docSize));
            requestJson.put("fileName", String.valueOf(fileName));
            requestJson.put("thumbnail", String.valueOf(thumbnail));
            requestJson.put("fileNameThumbnail", String.valueOf(fileNameThumbnail));
            requestJson.put("caption", String.valueOf(caption));
            requestJson.put("notification", String.valueOf(notification));
            requestJson.put("currentDate", String.valueOf(currentDate));

            Log.e(TAG, "@@@send_notification_apiResponse :" + "upper" + requestJson.toString());

            cz.msebera.android.httpclient.entity.StringEntity entity = new cz.msebera.android.httpclient.entity.StringEntity(requestJson.toString(), "UTF-8");

            client.post(mContext, BASE_URL + "EmojiController/send_notification_api", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "@@@send_notification_apiResponse :" + response.toString());
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void end_notification_api_group(Context mContext, String userName, String message, String senderId, String userName1, String profile, String sentTime, String deviceType, String uid, String message1, String time, String document, String dataType, String extension, String name, String phone, String micPhoto, String miceTiming, String userName2, String replytextData, String replyKey, String replyType, String replyOldData, String replyCrtPostion, String modelId, String receiverUid, String forwaredKey, String groupName, String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption, int notification, String currentDate, String senderTokenReplyPower, JSONArray group_members, long timestamp, ProgressBar progressBar, String imageWidth, String imageHeight, String aspectRatio, String selectionCount, ArrayList<selectionBunchModel> selectionBunch) {
        // Delegate to helper class to comply with Google Play's 16 KB page size requirement
        NotificationApiHelper.handleGroupNotification(mContext, userName, message, senderId, userName1, profile,
                sentTime, deviceType, uid, message1, time, document, dataType, extension, name,
                phone, micPhoto, miceTiming, userName2, replytextData, replyKey, replyType,
                replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey, groupName,
                docSize, fileName, thumbnail, fileNameThumbnail, caption, notification,
                currentDate, senderTokenReplyPower, group_members, timestamp, progressBar,
                imageWidth, imageHeight, aspectRatio, selectionCount,selectionBunch);
    }

    private static void putSafe(JSONObject json, String key, String value) throws JSONException {
        json.put(key, value != null ? value : "");
    }

    // Optimized truncateToWords method
    private static String truncateToWords(String message, int maxLength) {
        if (message.length() <= maxLength) return message;
        String trimmed = message.substring(0, Math.min(maxLength, message.length()));
        int lastSpace = trimmed.lastIndexOf(' ');
        return (lastSpace > 0 ? trimmed.substring(0, lastSpace) : trimmed) + "...";
    }

    // Callback interface for async results


//    public static void create_individual_chattingForGrp(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, chattingScreen personalmsgLimitMsg, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, ImageView cancel, chatAdapter chatAdapters, FirebaseDatabase database, String userFTokenKey, Activity mActivity, int notification, ArrayList<messageModel> messageList, File savedThumbnail, RecyclerView messageRecView, String deviceType) {
//        try {
//
//            Log.d(TAG, "create_individual_chatting: " + messages);
//            int count = 0;
//            Constant.setSfFunction(mContext);
//            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
//            Constant.setSF.apply();
//
//            Log.d("apihittime", String.valueOf(count + 1));
//            Log.d("captiondta", model.getCaption());
//
//
//            //  todo here is a code for webservice  first priority to firebase then webservice
//
//            client = new AsyncHttpClient();
//            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
//            final RequestParams request_param = new RequestParams();
//            try {
//                request_param.put("uid", senderId);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("friend_id", receiverUid);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("message", messages);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("user_name", user_name);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            //always keep 1
//            request_param.put("notification", String.valueOf(1));
//            if (upload_docs == null) {
//                request_param.put("upload_docs", "");
//            } else {
//
//                try {
//                    request_param.put("upload_docs", upload_docs);
//                } catch (Exception ex) {
//                    // Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            try {
//                request_param.put("dataType", dataType);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            Log.d("dataType", dataType);
//            try {
//                request_param.put("extension", extension);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("name", name);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("phone", phone);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            try {
//                request_param.put("micPhoto", profilepic);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            try {
//                request_param.put("miceTiming", miceTiming);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                request_param.put("sent_time", sent_time);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            Log.d("idddd", senderId + ":" + receiverUid + ":" + messages);
//            Log.d(TAG, "@@@create_individual_chatting :" + CREATE_INDIVIDUAL_CHATTING + "?" + request_param.toString());
//            ((Activity) mContext).runOnUiThread(new Runnable() {
//                public void run() {
//
//                    client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            Log.d(TAG, "@@@create_individual_chatting_response :" + response.toString());
//
//                            try {
//                                int status = response.getInt("error_code");
//
//                                if (status == 200) {
//                                    database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//
//                                            //chatAdapters.setPendingVisibility(messageList.size() - 1, View.GONE);
//
//
//                                            try {
//                                                // for sender data
//                                                chatAdapters.checkBarIsActive = 1;
//                                            } catch (Exception ignored) {
//                                            }
//                                            database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//                                                @Override
//                                                public void onSuccess(Void unused) {
//
//                                                    // todo remove last item
//                                                    try {
//                                                        chatAdapters.setLastItemVisible(false);
//                                                    } catch (Exception e) {
//
//                                                    }
//
//
//                                                    // for notify download
//                                                    mContext.registerReceiver(new DownloadReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED);
//                                                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
//                                                    }
//
//
//                                                    if (userFTokenKey != null) {
//
//
//                                                        Constant.getSfFuncion(mContext);
//                                                        String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");
//
//
//                                                        if (sleepKey.equals(Constant.sleepKey)) {
//
//                                                        } else {
//                                                            String STORED_KEY = EncryptionUtils.ENCRYPTION_KEY;
//                                                            SecretKey key;
//
//
//                                                            try {
//                                                                key = EncryptionUtils.decodeKeyFromBase64(STORED_KEY);
//                                                                String encodedKey = EncryptionUtils.encodeKeyToBase64(key);
//                                                                Log.d("TAG", "Generated Key (Base64): " + encodedKey);
//                                                                // todo   Secured key
//                                                                //  YBM7kODvEs6B5GydYTZI3w==
//
//                                                            } catch (Exception e) {
//                                                                throw new RuntimeException(e);
//                                                            }
//
//                                                            Log.d(TAG, "model.getDataType()" + model.getDataType());
//                                                            try {
//                                                                if (EncryptionUtils.decryptString(key, model.getDataType()).equals(Constant.Text)) {
//                                                                    //  Toast.makeText(mContext, "text", Toast.LENGTH_SHORT).show();
//                                                                    FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), model.getMessage(), mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType);
//
//                                                                    Log.d("543234", "userFTokenKey: " + userFTokenKey);
//                                                                    Log.d("543234", "model.getUserName(): " + model.getUserName());
//                                                                    Log.d("543234", "model.getMessage(): " + model.getMessage());
//                                                                    Log.d("543234", "senderId: " + senderId);
//                                                                    Log.d("543234", "user_name: " + user_name);
//                                                                    Log.d("543234", "Constant.getSF.getString(Constant.profilePic, \"\"): " + Constant.getSF.getString(Constant.profilePic, ""));
//                                                                    Log.d("543234", "deviceType: " + deviceType);
//
//                                                                    fcmNotificationSenderChatting.SendNotifications();
//
//                                                                } else if (EncryptionUtils.decryptString(key, model.getDataType()).equals(Constant.img)) {
//                                                                    //  Toast.makeText(mContext, "img ", Toast.LENGTH_SHORT).show();
//                                                                    FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), "You have a new Image", mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType);
//                                                                    fcmNotificationSenderChatting.SendNotifications();
//                                                                } else if (EncryptionUtils.decryptString(key, model.getDataType()).equals(Constant.contact)) {
//                                                                    FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), "You have a new Contact", mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType);
//                                                                    fcmNotificationSenderChatting.SendNotifications();
//                                                                } else if (EncryptionUtils.decryptString(key, model.getDataType()).equals(Constant.voiceAudio)) {
//                                                                    FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), "You have new a Audio", mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType);
//                                                                    fcmNotificationSenderChatting.SendNotifications();
//                                                                } else {
//                                                                    //Toast.makeText(mContext, "document ", Toast.LENGTH_SHORT).show();
//                                                                    FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), "You have new a File", mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType);
//                                                                    fcmNotificationSenderChatting.SendNotifications();
//                                                                }
//                                                            } catch (Exception e) {
//                                                                throw new RuntimeException(e);
//                                                            }
//                                                        }
//
//
//                                                    }
//
//
//                                                    String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();
//
//                                                    database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void unused) {
//                                                            String STORED_KEY = EncryptionUtils.ENCRYPTION_KEY;
//                                                            SecretKey key;
//
//                                                            try {
//                                                                key = EncryptionUtils.decodeKeyFromBase64(STORED_KEY);
//                                                                String encodedKey = EncryptionUtils.encodeKeyToBase64(key);
//                                                                Log.d("TAG", "Generated Key (Base64): " + encodedKey);
//                                                                // todo   Secured key
//                                                                //  YBM7kODvEs6B5GydYTZI3w==
//
//                                                            } catch (Exception e) {
//                                                                throw new RuntimeException(e);
//                                                            }
//
//
//                                                        }
//                                                    });
//
//
//                                                }
//                                            });
//                                        }
//                                    });
//
//
//                                } else if (status == 403) {
//                                    Toast.makeText(mContext, "limited for " + receiverUid, Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    String message = response.getString("message");
//                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//
//                                e.printStackTrace();
//
//                                Log.d("@@@ notSuccess: ", e.getMessage());
//                            }
//                            super.onSuccess(statusCode, headers, response);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            super.onFailure(statusCode, headers, responseString, throwable);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//                        }
//                    });
//                }
//            });
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }


    public static void create_individual_chattingForReply(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, FirebaseDatabase database, String userFTokenKey, int notification, String deviceType, String senderTokenReplyPower) {
        try {


            int count = 0;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
            Constant.setSF.apply();

            Log.d("apihittime", String.valueOf(count + 1));
            Log.d("captiondta", model.getCaption());

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            try {
                request_param.put("uid", senderId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("friend_id", receiverUid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("message", messages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("user_name", user_name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //always keep 1
            request_param.put("notification", String.valueOf(1));
            if (upload_docs == null) {
                request_param.put("upload_docs", "");
            } else {

                try {
                    request_param.put("upload_docs", upload_docs);
                } catch (Exception ex) {
                    // Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            try {
                request_param.put("dataType", dataType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("dataType", dataType);
            try {
                request_param.put("extension", extension);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("name", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("phone", phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("micPhoto", profilepic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("miceTiming", miceTiming);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("sent_time", sent_time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("model_id", model.getModelId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("idddd", senderId + ":" + receiverUid + ":" + messages);
            Log.d(TAG, "@@@create_individual_chattingReply:" + CREATE_INDIVIDUAL_CHATTING + "?" + request_param.toString());


            client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "@@@create_individual_chattingReplyxxx :" + response.toString());

                    try {
                        int status = response.getInt("error_code");
                        if (status == 200) {


                            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(200);

                            JSONObject data = response.getJSONObject("data");

                            // TODO: 05/08/24 We need to remove comment below after using live base url  " int limit_status = data.getInt("limit_status");  "
                            int limit_status = 0;
                            try {
                                limit_status = data.getInt("limit_status");
                            } catch (JSONException e) {
                                limit_status = 0;
                            }

//                                    int limit_status = 0;


                            if (limit_status == 1) {
                                //todo expired not eligible to send
                                String total_msg_limit = data.getString("total_msg_limit");


                            } else {
                                //todo can  eligible to send

                                database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        //chatAdapters.setPendingVisibility(messageList.size() - 1, View.GONE);


                                        database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                            @Override
                                            public void onSuccess(Void unused) {

                                                // todo remove last item
                                                Log.d(TAG, "@@@create_individual_chattingReplyxxx : 1");


                                                if (userFTokenKey != null) {


                                                    Constant.getSfFuncion(mContext);
                                                    String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                                                    if (sleepKey.equals(Constant.sleepKey)) {

                                                    } else {

                                                        Constant.getSfFuncion(mContext);
                                                        String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");
                                                        Log.d(TAG, "@@@create_individual_chattingReplyxxx : upper sndApi + " + fcm);
                                                        try {
                                                            if (model.getDataType().equals(Constant.Text)) {

                                                                Webservice.end_notification_apiReply(mContext, userFTokenKey, model.getUserName(), model.getMessage(), senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), senderTokenReplyPower);

                                                            } else if (model.getDataType().equals(Constant.img)) {

                                                                Webservice.end_notification_apiReply(mContext, userFTokenKey, model.getUserName(), "You have a new Image", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), senderTokenReplyPower);

                                                            } else if (model.getDataType().equals(Constant.contact)) {

                                                                Webservice.end_notification_apiReply(mContext, userFTokenKey, model.getUserName(), "You have a new Contact", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), senderTokenReplyPower);


                                                            } else if (model.getDataType().equals(Constant.voiceAudio)) {
                                                                Webservice.end_notification_apiReply(mContext, userFTokenKey, model.getUserName(), "You have new a Audio", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), senderTokenReplyPower);


                                                            } else if (model.getDataType().equals(Constant.video)) {
                                                                Webservice.end_notification_apiReply(mContext, userFTokenKey, model.getUserName(), "You have new a Video", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), fcm);


                                                            } else {

//                                                                        FcmNotificationSenderChatting fcmNotificationSenderChatting = new FcmNotificationSenderChatting(userFTokenKey, model.getUserName(), "You have new a File", mContext, mActivity, senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model);
//                                                                        fcmNotificationSenderChatting.SendNotifications();

                                                                Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a File", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(),

                                                                        model.getSelectionCount());

                                                            }
                                                        } catch (Exception e) {
                                                            //  throw new RuntimeException(e);
                                                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                    }


                                                } else {
                                                    Log.d(TAG, "@@@create_individual_chattingReplyxxx : empty");
                                                }


                                                String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();

                                                database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {


                                                        try {


                                                            // todo   Secured key
                                                            //  YBM7kODvEs6B5GydYTZI3w==

                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }


                                                    }
                                                });


                                            }
                                        });
                                    }
                                });


                            }


                        } else if (status == 403) {

                            // Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();


                        } else {
                            String message = response.getString("message");
                            //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();

                        Log.d("@@@ notSuccess: ", e.getMessage());
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    //
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    //


                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                    //


                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void create_individual_chattingForEmojiReact(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, FirebaseDatabase database, String userFTokenKey, String deviceType) {
        try {
            Log.d(TAG, "create_individual_chatting: " + messages);

            SecretKey key;
            try {


                // todo   Secured key
                //  YBM7kODvEs6B5GydYTZI3w==

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int count = 0;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
            Constant.setSF.apply();

            Log.d("apihittime", String.valueOf(count + 1));
            Log.d("captiondta", model.getCaption());

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            try {
                request_param.put("uid", senderId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("friend_id", receiverUid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("message", messages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("user_name", user_name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //always keep 1
            request_param.put("notification", String.valueOf(1));
            if (upload_docs == null) {
                request_param.put("upload_docs", "");
            } else {

                try {
                    request_param.put("upload_docs", upload_docs);
                } catch (Exception ex) {
                    // Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            try {
                request_param.put("dataType", dataType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("dataType", dataType);
            try {
                request_param.put("extension", extension);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("name", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("phone", phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("micPhoto", profilepic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("miceTiming", miceTiming);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("sent_time", sent_time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("model_id", model.getModelId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("idddd", senderId + ":" + receiverUid + ":" + messages);
            Log.d(TAG, "@@@create_individual_chatting :" + CREATE_INDIVIDUAL_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_individual_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    JSONObject data = response.getJSONObject("data");


                                    if (userFTokenKey != null) {


                                        Constant.getSfFuncion(mContext);
                                        String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                                        if (sleepKey.equals(Constant.sleepKey)) {

                                        } else {

                                            Log.d(TAG, "cabcjkqbkca: " + model.getDataType());

                                            String fcm = "";
                                            try {
                                                if (model.getDataType().equals(Constant.Text)) {

                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"" + model.getMessage() + "\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(),

                                                            model.getSelectionCount());

                                                } else if (model.getDataType().equals(Constant.img)) {


                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"Photo\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());

                                                } else if (model.getDataType().equals(Constant.contact)) {

                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"Contact\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                } else if (model.getDataType().equals(Constant.voiceAudio)) {
                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"Audio\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                } else if (model.getDataType().equals(Constant.video)) {
                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"Video\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                } else {

                                                    Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "Reacted " + messages + " to " + "\"File\"", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, deviceType, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());
                                                }
                                            } catch (Exception e) {
                                                //  throw new RuntimeException(e);
                                            }
                                        }


                                    }


                                    String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();

                                    database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {


                                            try {


                                                // todo   Secured key
                                                //  YBM7kODvEs6B5GydYTZI3w==

                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }


                                        }
                                    });


                                } else if (status == 403) {

                                    Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    //   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void create_group_chatting(Context mContext, String senderId, String group_id, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, grpChattingScreen personalmsgLimitMsg, group_messageModel model, String modelId, String created_by, String user_name, groupChatAdapter groupChatAdapters, messageModel model2, FirebaseDatabase database, ProgressBar progressBar) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", senderId);
            request_param.put("group_id", group_id);
            request_param.put("message", messages);
            request_param.put("user_name", user_name);

            try {
                if (upload_docs == null) {
                    request_param.put("upload_docs", "");
                } else {

                    try {
                        request_param.put("upload_docs", upload_docs);
                    } catch (Exception ex) {

                    }
                }
            } catch (Exception ignored) {
            }
            request_param.put("dataType", dataType);
            request_param.put("extension", extension);
            request_param.put("name", name);
            request_param.put("phone", phone);
            request_param.put("micPhoto", profilepic);
            request_param.put("miceTiming", miceTiming);
            request_param.put("sent_time", sent_time);
            request_param.put("created_by", created_by);
            request_param.put("model_id", modelId);


            Log.d("idddd", senderId + ":" + group_id + ":" + messages);
            Log.d(TAG, "@@@create_group_for_chatting :" + CREATE_GROUP_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_GROUP_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_group_for_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    ArrayList<unableDeliveredUserMdoel> list = new ArrayList<>();


                                    JSONObject data = response.getJSONObject("data");


                                    JSONArray not_receivers_usrs = data.getJSONArray("not_receivers_usrs");

                                    if (not_receivers_usrs.length() != 0) {
                                        for (int i = 0; i < not_receivers_usrs.length(); i++) {

                                            String u_id = not_receivers_usrs.getJSONObject(i).getString("u_id");
                                            String full_name = not_receivers_usrs.getJSONObject(i).getString("full_name");
                                            String p_img = not_receivers_usrs.getJSONObject(i).getString("p_img");

                                            Log.d("UNICS@@", "u_id: " + u_id);
                                            Log.d("UNICS@@", "full_name: " + full_name);
                                            Log.d("UNICS@@", "p_img: " + p_img);
                                            list.add(new unableDeliveredUserMdoel(u_id, full_name, p_img));


                                        }

                                        Constant.dialogueLayoutForAll(mContext, R.layout.messgae_lmt_dialogue);
                                        AppCompatImageView dismiss = Constant.dialogLayoutColor.findViewById(R.id.dismiss);
                                        RecyclerView recyclerview = Constant.dialogLayoutColor.findViewById(R.id.recyclerview);

                                        unableDeliveredUserAdaptyer adaptyer = new unableDeliveredUserAdaptyer(mContext, list);
                                        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
                                        recyclerview.setAdapter(adaptyer);
                                        adaptyer.notifyDataSetChanged();

                                        Constant.dialogLayoutColor.show();


                                        dismiss.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Constant.dialogLayoutColor.dismiss();
                                            }
                                        });


                                    }


                                    JSONArray group_members = data.getJSONArray("group_members");
                                    grpChattingScreen.database.getReference().child(Constant.GROUPCHAT).child(senderId + group_id).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            try {
                                                groupChatAdapters.setLastItemVisible(false);
                                            } catch (Exception e) {

                                            }

                                        }
                                    });


                                    Constant.getSfFuncion(mContext);
                                    String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                                    if (sleepKey.equals(Constant.sleepKey)) {

                                    } else {

                                        Log.d(TAG, "csvchjawbcawkbhcae : " + group_members.toString());


                                        try {

                                            Constant.setSfFunction(mContext);
                                            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
                                            Constant.setSF.apply();
                                            Constant.getSfFuncion(mContext);
                                            String fcm = Constant.getSF.getString(Constant.FCM_TOKEN, "");
                                            if (model2.getDataType().equals(Constant.Text)) {

                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), model2.getMessage(), senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());


                                            } else if (model2.getDataType().equals(Constant.img)) {
                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), "You have a new Image", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());

                                            } else if (model2.getDataType().equals(Constant.contact)) {
                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), "You have a new Contact", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());

                                            } else if (model2.getDataType().equals(Constant.voiceAudio)) {
                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), "You have new a Audio", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());

                                            } else if (model2.getDataType().equals(Constant.video)) {
                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), "You have new a Video", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());

                                            } else {
                                                Webservice.end_notification_api_group(mContext, model2.getUserName(), "You have new a File", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, "1", model2.getUid(), model2.getMessage(), model2.getTime(), model2.getDocument(), model2.getDataType(), model2.getExtension(), model2.getName(), model2.getPhone(), model2.getMicPhoto(), model2.getMiceTiming(), model2.getUserName(), model2.getReplytextData(), model2.getReplyKey(), model2.getReplyType(), model2.getReplyOldData(), model2.getReplyCrtPostion(), model2.getModelId(), model2.getReceiverUid(), model2.getForwaredKey(), model2.getGroupName(), model2.getDocSize(), model2.getFileName(), model2.getThumbnail(), model2.getFileNameThumbnail(), model2.getCaption(), model2.getNotification(), model2.getCurrentDate(), fcm, group_members, model2.getTimestamp(), progressBar, model2.getImageWidth(), model2.getImageHeight(), model2.getAspectRatio(), model2.getSelectionCount(), model2.getSelectionBunch());

                                            }
                                        } catch (Exception e) {
                                            // throw new RuntimeException(e);
                                        }
                                    }


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    Log.d("errrorMsg", message);
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            Log.d("responseString", responseString);
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });


        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    // Method to parse group members and initialize message models

    public static void get_individual_chatting(Context mContext, String senderId, String receiverUid, TextView limitStatus, TextView totalMsgLimit) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", senderId);
            request_param.put("friend_id", receiverUid);

            Log.d("uid+friendid", senderId + receiverUid);


            Log.d(TAG, "@@@get_individual_chatting :" + GET_INDIVIDUAL_ID + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    Constant.messageList.clear();
                    client.post(GET_INDIVIDUAL_ID, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_individual_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    JSONObject data = response.getJSONObject("data");

//                                    int limit_status = data.getInt("limit_status");
//                                    if (limit_status == 1) {
//                                        //todo expired not eligible to send
//                                        String total_msg_limit = data.getString("total_msg_limit");
//                                        limitStatus.setText(String.valueOf(limit_status));
//                                        totalMsgLimit.setText(total_msg_limit);
//
//                                    } else {
//                                        //todo can  eligible to send
//                                        limitStatus.setText(String.valueOf(limit_status));
//
//                                    }


                                } else {
                                    String message = response.getString("message");

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_individual_chattingUnion(String senderId, String receiverUid) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            RequestParams request_param = new RequestParams();
            request_param.put("uid", senderId);
            request_param.put("friend_id", receiverUid);

            Log.d(TAG, "@@@get_individual_chatting :" + GET_INDIVIDUAL_ID + "?" + request_param.toString());

            Constant.messageList.clear();
            client.post(GET_INDIVIDUAL_ID, request_param, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "@@@get_individual_chatting_response :" + response.toString());

                    try {
                        int status = response.getInt("error_code");
                        if (status == 200) {
                            // Handle success case
                        } else {
                            String message = response.getString("message");
                            // Handle error message
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("@@@ notSuccess: ", e.getMessage());
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get_user_active_chat_list(Context mContext, String uid, ChattingRoomUtils chattingRoom, RecyclerView inviterecyclerview, LinearLayout ecnlosure, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list, ProgressBar progressBar) {

        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setConnectTimeout(300000); // 5000 milliseconds (5 seconds)
            client.setResponseTimeout(300000);
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_active_chat_list :" + GET_USER_ACTIVE_CHAT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_ACTIVE_CHAT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_active_chat_list_response :" + response.toString());


                            try {
                                int status = response.getInt("error_code");
                                // Toast.makeText(mContext, String.valueOf(status), Toast.LENGTH_SHORT).show();
                                if (status == 200) {
                                    progressBar.setVisibility(View.GONE);


                                    String message = response.getString("message");

                                    JSONArray array = response.getJSONArray("data");
                                    get_user_active_contact_list.clear();
                                    Constant.get_user_active_contact_list.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);

                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String msg_limit = obj.getString("msg_limit");
                                        String caption = obj.getString("caption");
                                        String sent_time = obj.getString("sent_time");
                                        String dataType = obj.getString("dataType");
                                        String messages = obj.getString("message");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        String room = obj.getString("room");
                                        String original_name = obj.getString("original_name");
                                        int notification = obj.getInt("notification");
                                        boolean block = obj.optBoolean("block", false);
                                        boolean iamblocked = obj.optBoolean("iamblocked", false);

                                        Log.d("caption$", caption);

                                        Constant.get_user_active_contact_list.add(new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, notification, msg_limit, device_type, room, original_name, block, iamblocked));

                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_active_chat_list(mContext, uid, photo, full_name, mobile_no, caption, f_token, messages, dataType, sent_time, notification, msg_limit, device_type, original_name, block, iamblocked);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }
                                    }
                                    ecnlosure.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    inviterecyclerview.setVisibility(View.VISIBLE);

                                    chattingRoom.setAdapter(Constant.get_user_active_contact_list);

                                } else if (status == 201) {


                                    String message = response.getString("message");
                                    ecnlosure.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    inviterecyclerview.setVisibility(View.GONE);
                                    Constant.setSfFunction(mContext);
                                    Constant.setSF.putString("notiKey", "");
                                    Constant.setSF.apply();

                                    try {
                                        new DatabaseHelper(mContext).deleteAllChatting();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

//


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            //


            e.printStackTrace();
        }
    }


    public static void get_user_active_chat_list_for_msgLmt(Context mContext, String uid, MsgLimitUtils msgLimitFragment, RecyclerView recyclerView, ProgressBar progressBar, CardView noData) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_active_chat_list :" + GET_USER_ACTIVE_CHAT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_ACTIVE_CHAT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_active_chat_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_user_active_contact_listmsgLmt.clear();
                                    Constant.searchMsgLmt.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String msg_limit = obj.getString("msg_limit");
                                        String f_token = obj.getString("f_token");
                                        String messages = obj.getString("message");
                                        String dataType = obj.getString("dataType");
                                        String sent_time = obj.getString("sent_time");
                                        String device_type = obj.getString("device_type");
                                        String original_name = obj.getString("original_name");
                                        int notification = obj.getInt("notification");
                                        boolean block = obj.optBoolean("block", false);
                                        boolean iamblocked = obj.optBoolean("iamblocked", false);
                                        Log.d("caption$", caption);
                                        Constant.get_user_active_contact_listmsgLmt.add(new get_user_active_contact_list_MessageLmt_Model(photo, full_name, mobile_no, caption, uid, msg_limit, block));
                                        Constant.searchMsgLmt.add(new String(full_name));


                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_active_chat_list(mContext, uid, photo, full_name, mobile_no, caption, f_token, messages, dataType, sent_time, notification, msg_limit, device_type, original_name, block, iamblocked);
                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                        if (Constant.get_user_active_contact_listmsgLmt.size() == 0) {
                                            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                                            noData.setVisibility(View.VISIBLE);
                                        } else {
                                            noData.setVisibility(View.GONE);
                                        }

                                    }

                                    recyclerView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    msgLimitFragment.setAdapter(Constant.get_user_active_contact_listmsgLmt);


                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void set_message_limit_for_user_chat(Context mContext, String uid, String friend_id, String msg_limit, CardView cardview, EditText et1, CardView customToastCard, TextView customToastText) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("friend_id", friend_id);
            request_param.put("msg_limit", msg_limit);


            Log.d(TAG, "@@@set_message_limit_for_user_chat :" + SET_MSG_LIMIT + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SET_MSG_LIMIT, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@set_message_limit_for_user_chat_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");


                                    JSONObject obj = response.getJSONObject("data");
                                    String friend_id = obj.getString("friend_id");
                                    String msg_limit = obj.getString("msg_limit");
                                    Log.d("msgLmt", friend_id + " " + msg_limit);


                                    Constant.showCustomToast("Msg limit set for privacy in a day - " + msg_limit, customToastCard, customToastText);


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void set_message_limit_for_all_users(Context mContext, String uid, String msg_limit, MsgLimitUtils msgLimitFragment, RecyclerView recyclerView, CardView cardview, ProgressBar progressBar, CardView noData, EditText et1, CardView customToastCard, TextView customToastText) {

        try {
            Constant.get_user_active_contact_list.clear();
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("msg_limit", msg_limit);

            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.msg_limitFORALL, msg_limit).apply();


            Log.d(TAG, "@@@set_message_limit_for_all_users :" + SET_MSG_LIMIT_FOR_ALL + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SET_MSG_LIMIT_FOR_ALL, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@set_message_limit_for_all_users_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");

                                    Webservice.get_user_active_chat_list_for_msgLmt(mContext, uid, msgLimitFragment, recyclerView, progressBar, noData);


                                    Constant.showCustomToast("Msg limit set for privacy in a day - " + msg_limit, customToastCard, customToastText);


                                    try {

                                        Constant.getSfFuncion(mContext);
                                        String themColor = Constant.getSF.getString(Constant.ThemeColorKey, "#00A3E9");


                                    } catch (Exception ignored) {
                                    }


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_message_limit_for_all_users(Context mContext, String uid, MsgLimitUtils msgLimitFragment, TextView txt1) {


        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_message_limit_for_all_users :" + GET_MESSAGE_LIMT_FOR + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_MESSAGE_LIMT_FOR, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_message_limit_for_all_users_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    JSONObject obj = array.getJSONObject(0);
                                    String msg_limit = obj.getString("msg_limit");
                                    txt1.setText(msg_limit);


                                } else {
                                    String message = response.getString("message");
                                    // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void search_user_in_voice_call(Context mContext, String uid, String search_by, callFragment callFragments) {


        try {
            Constant.get_calling_contact_list.clear();
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("search_by", search_by);


            Log.d(TAG, "@@@search_user_in_voice_call :" + SEARCH_USER_IN_VOICE_AND_VIDEO_CALL + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEARCH_USER_IN_VOICE_AND_VIDEO_CALL, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@search_user_in_voice_call_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String full_name = obj.getString("full_name");
                                        String photo = obj.getString("photo");
                                        String caption = obj.getString("caption");
                                        String mobile_no = obj.getString("mobile_no");
                                        String uid = obj.getString("uid");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));
                                    }
                                    callFragments.setAdapter(Constant.get_contact_model);


                                } else {
                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void search_user_in_video_call(Context mContext, String uid, String search_by, videoCallFragment videoCallFragments) {


        try {
            Constant.get_calling_contact_list.clear();
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("search_by", search_by);


            Log.d(TAG, "@@@search_user_in_voice_call :" + SEARCH_USER_IN_VOICE_AND_VIDEO_CALL + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEARCH_USER_IN_VOICE_AND_VIDEO_CALL, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@search_user_in_voice_call_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String full_name = obj.getString("full_name");
                                        String photo = obj.getString("photo");
                                        String caption = obj.getString("caption");
                                        String mobile_no = obj.getString("mobile_no");
                                        String uid = obj.getString("uid");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));
                                    }
                                    videoCallFragments.setAdapter(Constant.get_contact_model);


                                } else {
                                    String message = response.getString("message");

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_user_active_chat_list_For_GRP(Context mContext, String uid, newGroupActivity newGroupActivity, ProgressBar progressBar, CardView noData) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_active_chat_list_For_GRP :" + GET_USER_ACTIVE_CHAT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_ACTIVE_CHAT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_active_chat_list_For_GRP_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_user_active_contact_list.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String msg_limit = obj.getString("msg_limit");
                                        String sent_time = obj.getString("sent_time");
                                        String dataType = obj.getString("dataType");
                                        String messages = obj.getString("message");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        String room = obj.getString("room");
                                        String original_name = obj.getString("original_name");
                                        int notification = obj.getInt("notification");
                                        boolean block = obj.optBoolean("block", false);
                                        boolean iamblocked = obj.optBoolean("iamblocked", false);
                                        Log.d("caption$", caption);
                                        Constant.get_user_active_contact_list.add(new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, notification, msg_limit, device_type, room, original_name, block, iamblocked));


                                        try {
                                            new DatabaseHelper(mContext).insert_get_user_active_chat_list(mContext, uid, photo, full_name, mobile_no, caption, f_token, messages, dataType, sent_time, notification, msg_limit, device_type, original_name, block, iamblocked);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }

                                    }

                                    com.Appzia.enclosure.Screens.newGroupActivity.grpRecyclerview.setVisibility(View.VISIBLE);

                                    newGroupActivity.setAdapter(Constant.get_user_active_contact_list);
                                    progressBar.setVisibility(View.GONE);

                                    if (Constant.get_user_active_contact_list.size() == 0) {
                                        noData.setVisibility(View.VISIBLE);
                                    } else {
                                        noData.setVisibility(View.GONE);
                                    }

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void create_group_for_chatting(Context mContext, String uid, String group_name, String invited_friend_list, File group_icon) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("group_name", group_name);
            request_param.put("invited_friend_list", invited_friend_list);
            Log.d("invited_friend_list&&", invited_friend_list);
            if (group_icon != null) {
                request_param.put("group_icon", group_icon);
            } else {
                request_param.put("group_icon", "");
            }

            Log.d(TAG, "@@@create_group_for_chatting :" + CREATE_GROUP_FOR_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_GROUP_FOR_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_group_for_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    //  Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                                    if (group_icon != null) {
                                        group_icon.delete();
                                        newGroupActivity.imageFile.delete();
                                        newGroupActivity.imageFile = null;
                                    } else {
                                    }
                                    ((Activity)mContext).onBackPressed();

                                    // TransitionHelper.performTransition(((Activity)mContext));


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_group_list(Context mContext, String uid, GroupMsgUtils groupMsgFragment, RecyclerView recyclerView, ProgressBar progressBar, CardView noData) {
        try {
            Retrofit retrofit = APIClient.getClient();
            API api = retrofit.create(API.class);
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("uid", uid);
            RequestBody requestBody = builder.build();


            api.get_group_list(requestBody).enqueue(new Callback<grp_list_model>() {
                @Override
                public void onResponse(@NonNull Call<grp_list_model> call, @NonNull Response<grp_list_model> response) {
                    Log.d(TAG, "ongroupResponse: " + response.code() + " : " + uid);

                    if (response.body() != null) {

                        String errorcode = response.body().getError_code();
                        String message = response.body().getMessage();

                        ArrayList<grp_list_child_model> data = new ArrayList<>();
                        if (errorcode.equals("200")) {
                            data = response.body().getData();

                            groupMsgFragment.setAdapter(data);

                            for (grp_list_child_model model : data) {

                                try {
                                    new DatabaseHelper(mContext).insert_get_group_listTable(mContext, model.getGroup_id(), model.getSr_nos(), model.getGroup_name(), model.getGroup_icon(), model.getGroup_created_by(), model.getF_token(), model.getGroup_members_count(), model.getGroup_members(), model.getSent_time(), String.valueOf(model.getDec_flg()), model.getL_msg());

                                } catch (Exception e) {
                                    Log.d("TAG", "onDataChange: " + e.getMessage());
                                }

                            }

                            progressBar.setVisibility(View.GONE);

//                            if (data.size() == 0) {
//                                noData.setVisibility(View.VISIBLE);
//                            } else {
//                                noData.setVisibility(View.GONE);
//                            }
                            noData.setVisibility(View.GONE);

                        } else {
                            noData.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                        noData.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + response.toString());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<grp_list_model> call, @NonNull Throwable t) {
                    //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("loginerror", t.getMessage());
                }
            });


        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_calling_contact_list(Context mContext, String uid, VideoCallUtil videoCallFragment, RecyclerView inviterecyclerview, ProgressBar progressBar, CardView noData) {
        try {


            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_calling_contact_list :" + GETTING_CALLING_CONTACT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GETTING_CALLING_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_calling_contact_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");
                                    ///   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_contact_model.clear();
                                    Constant.searchVideoCall.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);
                                        Constant.searchVideoCall.add(new String(full_name));


                                        //TODO INSERT TABLE FROM HERE

                                        try {
                                            new DatabaseHelper(mContext).insert_get_users_all_contactTable(mContext, uid, photo, full_name, mobile_no, caption, f_token, device_type, block);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                        // Constant.grpList.add(new grp_list_model(group_id, group_name, group_icon, group_members_count, time, group_created_by));
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));

                                    }

                                    inviterecyclerview.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    videoCallFragment.setAdapter(Constant.get_contact_model);


                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_calling_contact_list2(Context mContext, String uid, CallUtil videoCallFragment, RecyclerView inviterecyclerview, ProgressBar progressBar, CardView noData) {
        try {
            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_calling_contact_list :" + GETTING_CALLING_CONTACT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GETTING_CALLING_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_calling_contact_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    String message = response.getString("message");
                                    ///   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_contact_model.clear();
                                    Constant.searchVideoCall.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);
                                        Constant.searchVideoCall.add(new String(full_name));


                                        //TODO INSERT TABLE FROM HERE

                                        try {
                                            new DatabaseHelper(mContext).insert_get_users_all_contactTable(mContext, uid, photo, full_name, mobile_no, caption, f_token, device_type, block);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                        // Constant.grpList.add(new grp_list_model(group_id, group_name, group_icon, group_members_count, time, group_created_by));
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));

                                    }

                                    inviterecyclerview.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    videoCallFragment.setAdapter(Constant.get_contact_model);


                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void create_group_calling(Context mContext, String uid, String friend_id, String invited_friend_list, String date, String currentTime, String calling_flag, String end_time, String call_type) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("friend_id", friend_id);
            request_param.put("invited_friend_list", invited_friend_list);
            request_param.put("date", date);
            request_param.put("start_time", currentTime);
            request_param.put("calling_flag", calling_flag);
            request_param.put("end_time", end_time);
            request_param.put("call_type", call_type);

            Log.d("REQUEST_PARAM", "uid: " + uid);
            Log.d("REQUEST_PARAM", "friend_id: " + friend_id);
            Log.d("REQUEST_PARAM", "invited_friend_list: " + invited_friend_list);
            Log.d("REQUEST_PARAM", "date: " + date);
            Log.d("REQUEST_PARAM", "start_time: " + currentTime);
            Log.d("REQUEST_PARAM", "calling_flag: " + calling_flag);
            Log.d("REQUEST_PARAM", "end_time: " + end_time);
            Log.d("REQUEST_PARAM", "call_type: " + call_type);


            Log.d("currentTimeweb", currentTime);


            Log.d("senderUidVideoCall", uid + friend_id);
            // Toast.makeText(mContext, "misscall", Toast.LENGTH_SHORT).show();

            Log.d(TAG, "@@@create_group_callingnew :" + CREATE_GROUP_CALLLING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_GROUP_CALLLING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_group_calling_response :" + response.toString());
                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {
                                   // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext, "my" + message, Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Store last request details to prevent duplicates


    public static void create_group_callingMissCall(String uid, String friend_id, String invited_friend_list, String date, String currentTime, String calling_flag, String end_time, String call_type) {
        try {
            // Check for duplicate before making API request
            if (currentTime != null && currentTime.equals(lastCallTime) &&
                    call_type != null && call_type.equals(lastCallType) &&
                    lastStatus == 200) {
                Log.d(TAG, "Duplicate detected — skipping API call");
                return;
            }

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("friend_id", friend_id);
            request_param.put("invited_friend_list", invited_friend_list);
            request_param.put("date", date);
            request_param.put("start_time", currentTime);
            request_param.put("calling_flag", calling_flag);
            request_param.put("end_time", end_time);
            request_param.put("call_type", call_type);

            Log.d("currentTimeweb", currentTime);
            Log.d("senderUidVideoCall", uid + friend_id);
            Log.d(TAG, "@@@create_group_callingnew :" + CREATE_GROUP_CALLLING + "?" + request_param.toString());

            client.post(CREATE_GROUP_CALLLING, request_param, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "@@@create_group_calling_response :" + response.toString());
                    try {
                        int status = response.getInt("error_code");
                        String message = response.getString("message");

                        // Store last call details
                        lastCallTime = currentTime;
                        lastCallType = call_type;
                        lastStatus = status;

                        if (status == 200) {
                            // Success handling
                        } else {
                            // Error handling
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("@@@ notSuccess: ", e.getMessage());
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get_calling_contact_list_for_voiceCall(Context mContext, String uid, callFragment callFragment, RecyclerView inviterecyclerview, ProgressBar progressBar, CardView noData) {
        try {

            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_calling_contact_list :" + GETTING_CALLING_CONTACT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GETTING_CALLING_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_calling_contact_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    ///   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    JSONArray array = response.getJSONArray("data");
                                    Constant.searchVoiceCall.clear();
                                    Constant.get_contact_model.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);

                                        Log.d(TAG, "device_type: " + device_type);

                                        // Constant.grpList.add(new grp_list_model(group_id, group_name, group_icon, group_members_count, time, group_created_by));
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));

                                        Constant.searchVoiceCall.add(new String(full_name));


                                        //TODO INSERT TABLE FROM HERE

                                        try {
                                            new DatabaseHelper(mContext).insert_get_users_all_contactTable(mContext, uid, photo, full_name, mobile_no, caption, f_token, device_type, block);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                    }

                                    callFragment.setAdapter(Constant.get_contact_model);
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    noData.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void get_calling_contact_list_for_multiple_voiceCall(Context mContext, String uid, CallActivity callActivity) {
        try {

            client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_calling_contact_list :" + GETTING_CALLING_CONTACT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GETTING_CALLING_CONTACT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_calling_contact_list_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {


                                    String message = response.getString("message");
                                    ///   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    JSONArray array = response.getJSONArray("data");
                                    Constant.searchVoiceCall.clear();
                                    Constant.get_contact_model.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String f_token = obj.getString("f_token");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);

                                        Log.d(TAG, "device_type: " + device_type);

                                        // Constant.grpList.add(new grp_list_model(group_id, group_name, group_icon, group_members_count, time, group_created_by));
                                        Constant.get_contact_model.add(new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type, block));

                                        Constant.searchVoiceCall.add(new String(full_name));


                                        //TODO INSERT TABLE FROM HERE

                                        try {
                                            new DatabaseHelper(mContext).insert_get_users_all_contactTable(mContext, uid, photo, full_name, mobile_no, caption, f_token, device_type, block);

                                        } catch (Exception e) {
                                            Log.d("TAG", "onDataChange: " + e.getMessage());
                                        }


                                    }

                                    // callActivity.setAdapter(Constant.get_contact_model, recyclerview);


                                } else {

                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get_call_log(Context mContext, String uid, VideoCallUtil fragment, RecyclerView recyclerview, ProgressBar progressBar, CardView noData) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        RequestBody requestBody = builder.build();

        api.get_call_log_1(requestBody).enqueue(new Callback<get_call_log_1Model>() {
            @Override
            public void onResponse(@NonNull Call<get_call_log_1Model> call, @NonNull Response<get_call_log_1Model> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();

                    if (errorcode.equals("200")) {
                        ArrayList<get_call_log_1Child> model = response.body().getData();
                        if (model != null) {
                            recyclerview.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            if (model.isEmpty()) {
                                noData.setVisibility(View.VISIBLE);
                            } else {
                                noData.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.GONE);

                                if (model.get(0).getUser_info().isEmpty()) {
                                    noData.setVisibility(View.VISIBLE);
                                    recyclerview.setVisibility(View.GONE);
                                } else {
                                    recyclerview.setVisibility(View.VISIBLE);
                                }
                            }

                            fragment.setAdapterLog(model);

                        } else {
                            noData.setVisibility(View.GONE);
                        }

                        // Toast.makeText(mContext, "200", Toast.LENGTH_SHORT).show();


                    } else {
                        //Toast.makeText(mContext, "false", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<get_call_log_1Model> call, @NonNull Throwable t) {
                Log.e("loginerror", t.getMessage() + "my");
            }
        });
    }


    public static void get_call_log_history(Context mContext, String uid, String f_id, String xdate, VideoCallUtil videoCallFragment, ArrayList<call_log_history_model> call_log_history_list) {
        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_call_log :" + GET_CALL_LOG + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GET_CALL_LOG, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_call_log_response :" + response.toString());

                            try {
                                String message = response.getString("message");
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    JSONArray array = response.getJSONArray("data");
                                    String date = null;
                                    int sr_nos = 0;

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        date = obj.getString("date");
                                        sr_nos = obj.getInt("sr_nos");

                                        JSONArray user_infoArry = obj.getJSONArray("user_info");

                                        //  call_log_history_list.clear();
                                        for (int j = 0; j < user_infoArry.length(); j++) {

                                            JSONObject userObj = user_infoArry.getJSONObject(j);

                                            String friend_id = userObj.getString("friend_id");
                                            String jdate = userObj.getString("date");


                                            if (f_id.equals(friend_id) && jdate.equals(xdate)) {

                                                // from here new data found
                                                JSONArray call_history = userObj.getJSONArray("call_history");

                                                for (int k = 0; k < call_history.length(); k++) {

                                                    JSONObject nobj = call_history.getJSONObject(k);
                                                    String id = nobj.getString("id");
                                                    String uid = nobj.getString("uid");
                                                    String kfriend_id = nobj.getString("friend_id");
                                                    String kdate = nobj.getString("date");
                                                    String start_time = nobj.getString("start_time");
                                                    String end_time = nobj.getString("end_time");
                                                    String calling_flag = nobj.getString("calling_flag");
                                                    String call_type = nobj.getString("call_type");
                                                    // here apply final  list
                                                    Log.d("key0909", id);

                                                    call_log_history_list.add(new call_log_history_model(id, uid, kfriend_id, kdate, start_time, end_time, calling_flag, call_type));


                                                }


                                                if(call_log_history_list!=null){
                                                    videoCallFragment.setCallHistoryAdapter(call_log_history_list);

                                                }


                                            }


                                        }


                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_voice_call_log_for_voice_call(Context mContext, String uid, CallUtil fragment, RecyclerView recyclerview, ProgressBar progressBar, CardView noData) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        RequestBody requestBody = builder.build();

        api.get_voice_call_log_for_voice_call(requestBody).enqueue(new Callback<get_call_log_1Model>() {
            @Override
            public void onResponse(@NonNull Call<get_call_log_1Model> call, @NonNull Response<get_call_log_1Model> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();

                    if (errorcode.equals("200")) {
                       // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        ArrayList<get_call_log_1Child> model = response.body().getData();
                        if (model != null) {
                            recyclerview.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            if (model.isEmpty()) {
                                noData.setVisibility(View.VISIBLE);
                            } else {
                                noData.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.GONE);

                                if (model.get(0).getUser_info().isEmpty()) {
                                    noData.setVisibility(View.VISIBLE);
                                    recyclerview.setVisibility(View.GONE);
                                } else {
                                    recyclerview.setVisibility(View.VISIBLE);
                                }
                            }

                            fragment.setAdapterLog(model);

                        } else {
                            noData.setVisibility(View.GONE);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }


                } else {
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<get_call_log_1Model> call, @NonNull Throwable t) {
                //   Log.e("loginerror", t.getMessage());
            }
        });
    }


    public static void create_voice_calling_for_voice(Context mContext, String uid, String friend_id, String invited_friend_list, String date, String currentTime, String calling_flag, String end_time, String call_type) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("friend_id", friend_id);
            request_param.put("invited_friend_list", invited_friend_list);
            request_param.put("date", date);
            request_param.put("start_time", currentTime);
            request_param.put("calling_flag", calling_flag);
            request_param.put("end_time", end_time);
            request_param.put("call_type", call_type);

            Log.d("currentTime", currentTime);

            Log.d("senderUidVideoCall", uid + friend_id);

            Log.d(TAG, "@@@create_voice_calling_for_voice :" + CREATE_VOICE_CALLING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_VOICE_CALLING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_voice_calling_for_voice_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                String message = response.getString("message");
                                if (status == 200) {

                                    //   Toast.makeText(mContext, "my" + message, Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_voice_call_log_history_for_voice(Context mContext, String uid, String f_id, String xdate, CallUtil callFragment, ArrayList<call_log_history_model> call_log_history_list) {
        try {

//            callHistoryShimmer.setVisibility(View.VISIBLE);
//            callHistoryShimmer.startShimmer();

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);

            Log.d(TAG, "@@@get_voice_call_log_history_for_voice :" + GET_VOICE_CALL_LOG + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GET_VOICE_CALL_LOG, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_voice_call_log_history_for_voice_response :" + response.toString());

                            try {
                                String message = response.getString("message");
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    JSONArray array = response.getJSONArray("data");
                                    String date = null;
                                    int sr_nos = 0;
                                    call_log_history_list.clear();
                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        date = obj.getString("date");
                                        sr_nos = obj.getInt("sr_nos");

                                        JSONArray user_infoArry = obj.getJSONArray("user_info");


                                        for (int j = 0; j < user_infoArry.length(); j++) {

                                            JSONObject userObj = user_infoArry.getJSONObject(j);

                                            String friend_id = userObj.getString("friend_id");
                                            String jdate = userObj.getString("date");


                                            if (f_id.equals(friend_id) && jdate.equals(xdate)) {

                                                // from here new data found
                                                JSONArray call_history = userObj.getJSONArray("call_history");

                                                for (int k = 0; k < call_history.length(); k++) {

                                                    JSONObject nobj = call_history.getJSONObject(k);
                                                    String id = nobj.getString("id");
                                                    String uid = nobj.getString("uid");
                                                    String kfriend_id = nobj.getString("friend_id");
                                                    String kdate = nobj.getString("date");
                                                    String start_time = nobj.getString("start_time");
                                                    String end_time = nobj.getString("end_time");
                                                    String calling_flag = nobj.getString("calling_flag");
                                                    String call_type = nobj.getString("call_type");
                                                    // here apply final  list
                                                    Log.d("key0909", id);

                                                    call_log_history_list.add(new call_log_history_model(id, uid, kfriend_id, kdate, start_time, end_time, calling_flag, call_type));


                                                    try {
                                                        new DatabaseHelper(mContext).insert_gget_voice_call_logChild2Table(mContext, id, uid, kfriend_id, kdate, start_time, end_time, calling_flag, call_type);

                                                    } catch (Exception e) {
                                                        Log.d("TAG", "onDataChange: " + e.getMessage());
                                                    }

                                                }


                                                callFragment.setCallHistoryAdapter(call_log_history_list);

                                            }


                                        }


                                    }


                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void clear_voice_calling_list(Context mContext, String uid, CallUtil callFragment2, CardView noData, String call_type) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("call_type", call_type);

            Log.d(TAG, "@@@clear_voice_calling_list :" + CLEAR_VOICE_CALL_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(CLEAR_VOICE_CALL_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@clear_voice_calling_list_response :" + response.toString());

                            try {
                                String message = response.getString("message");
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    //   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    callFragment.get_call_log_1ChildList.clear();
                                    callFragment.list1.clear();
                                    callFragment.list2.clear();
                                    callFragment.list3.clear();
                                    callFragment.list4.clear();
                                    callFragment.list5.clear();
                                    callFragment.list6.clear();
                                    callFragment.list7.clear();
                                    callFragment.list8.clear();
                                    callFragment.list9.clear();
                                    callFragment.list10.clear();
                                    callFragment.list11.clear();
                                    callFragment.list12.clear();
                                    callFragment.list13.clear();
                                    callFragment.list14.clear();
                                    callFragment2.setAdapterLog(callFragment.get_call_log_1ChildList);

                                    // todo clear all data inside offline

                                    try {
                                        new DatabaseHelper(mContext).deleteVoiceCallLog();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    noData.setVisibility(View.VISIBLE);

                                } else {

                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }



    public static void clear_video_calling_list(Context mContext, String uid, VideoCallUtil videoCallFragment2, CardView noData, String call_type) {

        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("call_type", call_type);

            Log.d(TAG, "@@@clear_video_calling_list :" + CLEAR_VIDEO_CALLING_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(CLEAR_VIDEO_CALLING_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@clear_video_calling_list_response :" + response.toString());

                            try {
                                String message = response.getString("message");
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    //   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    videoCallFragment.get_call_log_1ChildList.clear();
                                    videoCallFragment.list1.clear();
                                    videoCallFragment.list2.clear();
                                    videoCallFragment.list3.clear();
                                    videoCallFragment.list4.clear();
                                    videoCallFragment.list5.clear();
                                    videoCallFragment.list6.clear();
                                    videoCallFragment.list7.clear();
                                    videoCallFragment.list8.clear();
                                    videoCallFragment.list9.clear();
                                    videoCallFragment.list10.clear();
                                    videoCallFragment.list11.clear();
                                    videoCallFragment.list12.clear();
                                    videoCallFragment.list13.clear();
                                    videoCallFragment.list14.clear();
                                    videoCallFragment2.setAdapterLog(videoCallFragment.get_call_log_1ChildList);

                                    try {
                                        new DatabaseHelper(mContext).deleteVideoCallLog();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    noData.setVisibility(View.VISIBLE);
                                } else {

                                    //    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_user_active_chat_list_forward(Context mContext, String uid, chatAdapter chatadapters, ProgressBar progressBar, RecyclerView recyclerview) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_active_chat_list_forward :" + GET_USER_ACTIVE_CHAT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_ACTIVE_CHAT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_active_chat_list_forward_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    recyclerview.setVisibility(View.VISIBLE);

                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_user_active_contact_forward_list.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String msg_limit = obj.getString("msg_limit");
                                        String sent_time = obj.getString("sent_time");
                                        String dataType = obj.getString("dataType");
                                        String messages = obj.getString("message");
                                        String f_token = obj.getString("f_token");
                                        String room = obj.getString("room");
                                        String original_name = obj.getString("original_name");
                                        String device_type = obj.getString("device_type");
                                        boolean block = obj.optBoolean("block", false);
                                        boolean iamblocked = obj.optBoolean("iamblocked", false);
                                        Log.d("caption$0", caption);
                                        Constant.get_user_active_contact_forward_list.add(new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, 0, msg_limit, device_type, room, original_name, block, iamblocked));
                                    }
                                    chatadapters.setAdapter(Constant.get_user_active_contact_forward_list);


                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

//


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            //


            e.printStackTrace();
        }
    }

    public static void get_user_active_chat_list_ShareContact(Context mContext, String uid, shareExternalDataCONTACTScreen chatadapters, RecyclerView recyclerview) {

        try {


            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);


            Log.d(TAG, "@@@get_user_active_chat_list_forward :" + GET_USER_ACTIVE_CHAT_LIST + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(GET_USER_ACTIVE_CHAT_LIST, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_user_active_chat_list_forward_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    recyclerview.setVisibility(View.VISIBLE);

                                    String message = response.getString("message");


                                    JSONArray array = response.getJSONArray("data");
                                    Constant.get_user_active_contact_forward_list.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        String uid = obj.getString("uid");
                                        String photo = obj.getString("photo");
                                        String full_name = obj.getString("full_name");
                                        String mobile_no = obj.getString("mobile_no");
                                        String caption = obj.getString("caption");
                                        String msg_limit = obj.getString("msg_limit");
                                        String sent_time = obj.getString("sent_time");
                                        String dataType = obj.getString("dataType");
                                        String messages = obj.getString("message");
                                        String device_type = obj.getString("device_type");
                                        String f_token = obj.getString("f_token");
                                        String room = obj.getString("room");
                                        String original_name = obj.getString("original_name");
                                        boolean block = obj.optBoolean("block", false);
                                        boolean iamblocked = obj.optBoolean("iamblocked", false);
                                        Log.d("caption$0", caption);
                                        Constant.get_user_active_contact_forward_list.add(new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, 0, msg_limit, device_type, room, original_name, block, iamblocked));

                                    }
                                    chatadapters.setAdapter(Constant.get_user_active_contact_forward_list);


                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

//


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void delete_individual_user_chatting(Context mContext, String uid, String recId, FirebaseDatabase database, get_user_active_chat_list_adapter getUserActiveChatListAdapters, int adapterPosition, ArrayList<get_user_active_contact_list_Model> get_user_active_contact_list, LinearLayout enclosure, View itemView, Dialog dialogLayoutColor) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            request_param.put("friend_id", recId);
            //  Toast.makeText(mContext, request_param.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "@@@DELETE_INDIVIDUAL_USER_CHATTING :" + DELETE_INDIVIDUAL_USER_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(DELETE_INDIVIDUAL_USER_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@DELETE_INDIVIDUAL_USER_CHATTING_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    // todo delete from sqlite
                                    try {
                                        new DatabaseHelper(mContext).deleteIndividuaChatting(recId);
                                    } catch (Exception e) {

                                    }

                                    dialogLayoutColor.dismiss();

                                    getUserActiveChatListAdapters.removeItem(adapterPosition, itemView);

                                    if (get_user_active_contact_list.isEmpty()) {
                                        enclosure.setVisibility(View.VISIBLE);

                                    } else {
                                        enclosure.setVisibility(View.GONE);
                                    }


                                    Constant.getSfFuncion(mContext);
                                    String sId = Constant.getSF.getString(Constant.UID_KEY, "");
                                    final String receiverRoom = recId + sId;

                                    Webservice.delete_sender_all_msg(mContext, sId, recId, receiverRoom);


                                    database.getReference().child("chats").child(receiverRoom).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //    Toast.makeText(mContext, "Deleted Message", Toast.LENGTH_SHORT).show();


                                        }
                                    });

                                } else {
                                    String message = response.getString("message");
                                    //   Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

//


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            //


            e.printStackTrace();
        }
    }


    public static void upload_user_contact_listRetrofitFIRST(Context mContext, String uidKey, File imageFile, ProgressDialog progressBar, String fileName, String country_codeKey) {

        String uploadUrl = BASE_URL + "upload_user_contact_list";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            if (imageFile != null) {
                params.put("cjson", imageFile);
            }
            params.put("uid", uidKey);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Progress Dialog
        if (progressBar != null) {
            progressBar.setMessage("Uploading...");

            progressBar.show();
        }

        // Execute Request
        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("Upload Success", "Response: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int errorCode = jsonResponse.getInt("error_code");
                    String message = jsonResponse.getString("message");

                    if (errorCode == 200) {
                        Webservice.contact_file_save(mContext, fileName, country_codeKey, progressBar, "", inviteScreen.dialog);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Response parsing error!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Upload Error", "Error: " + error.getMessage());
               // Toast.makeText(mContext, "Upload Failed!", Toast.LENGTH_SHORT).show();

                if (progressBar != null && progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
    }

    private static void contact_file_save(Context mContext, String contact_file_save, String country_codeKey, ProgressDialog progressBar, String invite, ProgressDialog dialog) {
        // Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = null;
        if (!contact_file_save.equals("")) {
            call = api.saveContactFile(contact_file_save);
        } else {
            Toast.makeText(mContext, "contact file is empty", Toast.LENGTH_SHORT).show();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //   Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_SUCCESS", "Responsefff: " + responseBody);
                        progressBar.dismiss();
                        //  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                        if (invite.equals("invite")) {
                            dialog.dismiss();
                        } else {
                            Intent intent = new Intent(mContext, lockScreen2.class);
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            SwipeNavigationHelper.finishWithSwipe((Activity) mContext);
                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString(Constant.loggedInKey, Constant.loggedInKey);
                            Constant.setSF.putString(Constant.COUNTRY_CODE, country_codeKey);
                            Constant.setSF.apply();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //   Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Failure: " + t.getMessage());
            }
        });
    }

    private static void contact_file_save_2(Context mContext, String contact_file_save, String country_codeKey, ProgressDialog progressBar, String invite, ProgressDialog dialog, inviteScreen inviteScreen, String uidKey, LinearLayout clearcalllog, CardView valuable, TextView textCard, ProgressBar progressBarMain) {
        // Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = null;
        if (!contact_file_save.equals("")) {
            call = api.saveContactFile(contact_file_save);
        } else {
            Toast.makeText(mContext, "contact file is empty", Toast.LENGTH_SHORT).show();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //   Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_SUCCESS", "Responsefff: " + responseBody);
                        progressBar.dismiss();
                        //  Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                        if (invite.equals("invite")) {
                            dialog.dismiss();
                        } else {
                            Intent intent = new Intent(mContext, lockScreen2.class);
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                            SwipeNavigationHelper.finishWithSwipe((Activity) mContext);
                            Constant.setSfFunction(mContext);
                            Constant.setSF.putString(Constant.loggedInKey, Constant.loggedInKey);
                            Constant.setSF.putString(Constant.COUNTRY_CODE, country_codeKey);
                            Constant.setSF.apply();
                        }

                        getDataFromAPIInitial(mContext, uidKey, inviteScreen, progressBarMain, clearcalllog, valuable, textCard, 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //   Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Failure: " + t.getMessage());
            }
        });
    }


    private static void contact_file_save_Main(Context mContext, String contact_file_save, String country_codeKey, MainActivityOld inviteScreen, String uidKey) {
        // Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = null;
        if (!contact_file_save.equals("")) {
            call = api.saveContactFile(contact_file_save);
        } else {
            Toast.makeText(mContext, "contact file is empty", Toast.LENGTH_SHORT).show();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //   Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_SUCCESS", "Responsefff: " + responseBody);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //   Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Failure: " + t.getMessage());
            }
        });
    }

//    public static void upload_user_contact_listRetrofitTwo(Context mContext, String uidKey, File imageFile, String mob_otpKey, String countryCodeKey, String token, String deviceId, inviteScreen inviteScreen, ProgressDialog progressbar, LinearLayout clearcalllog, String invite) {
//
//        Retrofit retrofit = APIClient.getClient();
//        API api = retrofit.create(API.class);
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//
//        MultipartBody.Part pickImageFile = null;
//        if (imageFile != null) {
//            RequestBody requestBodyImageFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
//            pickImageFile = MultipartBody.Part.createFormData("cjson", imageFile.getName(), requestBodyImageFile);
//        }
//
//        builder.setType(MultipartBody.FORM);
//        if (imageFile != null) {
//            builder.addPart(pickImageFile);
//        }
//        builder.addFormDataPart("uid", uidKey);
//        RequestBody requestBody = builder.build();
//
//        api.upload_user_contact_list(requestBody).enqueue(new Callback<globalModel>() {
//            @Override
//            public void onResponse(@NonNull Call<globalModel> call, @NonNull Response<globalModel> response) {
//                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());
//
//                if (response.body() != null) {
//
//                    int errorcode = response.body().getError_code();
//                    String message = response.body().getMessage();
//
//
//                    if (errorcode == 200) {
//                        inviteScreen.dialog.dismiss();
//                        Webservice.contact_file_save(mContext, imageFile.getName(), countryCodeKey, progressbar, invite, inviteScreen.dialog);
//
//                    } else {
//                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } else {
//                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onResponse: " + response.toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<globalModel> call, @NonNull Throwable t) {
//                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("loginerror", t.getMessage());
//            }
//        });
//    }


    public static void upload_user_contact_listRetrofitTwo(Context mContext, String uidKey, File imageFile, String mob_otpKey, String countryCodeKey, String token, String deviceId, inviteScreen inviteScreen, ProgressDialog progressbar, LinearLayout clearcalllog, String invite, CardView valuable, TextView textCard, ProgressBar progressBarMain) {

        String uploadUrl = BASE_URL + "upload_user_contact_list"; // Replace with your actual API URL
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        try {
            // Attach Image File
            if (imageFile != null) {
                params.put("cjson", imageFile);
            }

            // Add Other Parameters
            params.put("uid", uidKey);
            params.put("mob_otp", mob_otpKey);
            params.put("country_code", countryCodeKey);
            params.put("token", token);
            params.put("device_id", deviceId);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Progress Dialog
        if (progressbar != null) {
            progressbar.setMessage("Uploading...");
            progressbar.show();
        }

        // Execute Request
        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("Upload Success", "Response: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int errorCode = jsonResponse.getInt("error_code");
                    String message = jsonResponse.getString("message");

                    if (errorCode == 200) {
                        inviteScreen.dialog.dismiss();
                        Webservice.contact_file_save_2(mContext, imageFile.getName(), countryCodeKey, progressbar, invite, inviteScreen.dialog, inviteScreen, uidKey, clearcalllog, valuable, textCard, progressBarMain);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Response parsing error!", Toast.LENGTH_SHORT).show();
                }

                if (progressbar != null && progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Upload Error", "Error: " + error.getMessage());
               // Toast.makeText(mContext, "Upload Failed!", Toast.LENGTH_SHORT).show();

                if (progressbar != null && progressbar.isShowing()) {
                    progressbar.dismiss();
                }
            }
        });
    }


    public static void upload_user_contact_listRetrofitMainActivity(Context mContext, String uidKey, File imageFile, String mob_otpKey, String countryCodeKey, String token, String deviceId, MainActivityOld inviteScreen) {

        //  Toast.makeText(mContext, "Mainactivity", Toast.LENGTH_SHORT).show();
        String uploadUrl = BASE_URL + "upload_user_contact_list"; // Replace with your actual API URL
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        try {
            // Attach Image File
            if (imageFile != null) {
                params.put("cjson", imageFile);
            }

            // Add Other Parameters
            params.put("uid", uidKey);
            params.put("mob_otp", mob_otpKey);
            params.put("country_code", countryCodeKey);
            params.put("token", token);
            params.put("device_id", deviceId);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "File not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Execute Request
        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("Upload Success", "Response: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int errorCode = jsonResponse.getInt("error_code");
                    String message = jsonResponse.getString("message");

                    if (errorCode == 200) {

                        Webservice.contact_file_save_Main(mContext, imageFile.getName(), countryCodeKey, inviteScreen, uidKey);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Response parsing error!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Upload Error", "Error: " + error.getMessage());
            //    Toast.makeText(mContext, "Upload Failed!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public static void upload_user_contact_listRetrofitThree(Context mContext, String uidKey, String s, String mob_otpKey, String countryCodeKey, String token, String deviceId, MainActivityOld inviteScreen) {


        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uidKey);
        builder.addFormDataPart("user_contacts", s);
        builder.addFormDataPart("mobile_no", s);
        RequestBody requestBody = builder.build();

        api.upload_user_contact_list(requestBody).enqueue(new Callback<globalModel>() {
            @Override
            public void onResponse(@NonNull Call<globalModel> call, @NonNull Response<globalModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    int errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode == 200) {

                        //inviteScreen.getDataFromAPIInitial(mContext, uidKey, inviteScreen);

                        //  Webservice.verify_mobile_otp(mContext, uidKey, mob_otpKey, countryCodeKey, token, deviceId);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<globalModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });
    }

    public static void change_number(Context mContext, String uid, String mobileNoOld, String newNumberKey, EditText ninty1) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("mobile_no_old", mobileNoOld);
        builder.addFormDataPart("mobile_no_new", newNumberKey);
        RequestBody requestBody = builder.build();

        api.change_number(requestBody).enqueue(new Callback<change_numberModel>() {
            @Override
            public void onResponse(@NonNull Call<change_numberModel> call, @NonNull Response<change_numberModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode.equals("200")) {

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                        Constant.getSfFuncion(mContext);
                        String uidKey = Constant.getSF.getString(Constant.UID_KEY, "");


                        Intent intent = new Intent(mContext, otpverifyScreen.class);
                        intent.putExtra("phoneKy", newNumberKey);
                        intent.putExtra("uidKey", uidKey);

                        intent.putExtra("country_codeKey", ninty1.getText().toString());
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<change_numberModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });

    }

    public static void change_numberResend(Context mContext, String uid, String mobileNoOld, String newNumberKey) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("mobile_no_old", mobileNoOld);
        builder.addFormDataPart("mobile_no_new", newNumberKey);
        RequestBody requestBody = builder.build();

        api.change_number(requestBody).enqueue(new Callback<change_numberModel>() {
            @Override
            public void onResponse(@NonNull Call<change_numberModel> call, @NonNull Response<change_numberModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode.equals("200")) {

                        //Toast.makeText(mContext, response.body().getOtp(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<change_numberModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });

    }


    public static void verify_otp_for_delete_user(Context mContext, String uid, String otp) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("otp", otp);
        RequestBody requestBody = builder.build();

        api.verify_otp_for_delete_user(requestBody).enqueue(new Callback<change_numberModel>() {
            @Override
            public void onResponse(@NonNull Call<change_numberModel> call, @NonNull Response<change_numberModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode.equals("200")) {

                        Constant.dialogueLayoutForAll(mContext, R.layout.delete_ac_dialogue);
                        Constant.dialogLayoutColor.show();
                        AppCompatButton cancel = Constant.dialogLayoutColor.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constant.dialogLayoutColor.dismiss();
                            }
                        });
                        AppCompatButton sure = Constant.dialogLayoutColor.findViewById(R.id.sure);

                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Webservice.delete_my_account(mContext, uid);
                            }
                        });


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<change_numberModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });

    }

    public static void verify_otp_for_forgetOtp(Context mContext, String uid, String otp, String phoneKy) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("otp", otp);
        RequestBody requestBody = builder.build();

        api.verify_otp_for_delete_user(requestBody).enqueue(new Callback<change_numberModel>() {
            @Override
            public void onResponse(@NonNull Call<change_numberModel> call, @NonNull Response<change_numberModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode.equals("200")) {

                        Webservice.forget_lock_screen(mContext, uid, phoneKy);


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<change_numberModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });

    }

    private static void delete_my_account(Context mContext, String uid) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        RequestBody requestBody = builder.build();

        api.delete_my_account(requestBody).enqueue(new Callback<change_numberModel>() {
            @Override
            public void onResponse(@NonNull Call<change_numberModel> call, @NonNull Response<change_numberModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();


                    if (errorcode.equals("200")) {

                        Constant.setSfFunction(mContext);
                        Constant.setSF.putString(Constant.UID_KEY, "0");
                        Constant.setSF.putString(Constant.PHONE_NUMBERKEY, "");
                        Constant.setSF.putString(Constant.COUNTRY_CODE, "");
                        Constant.setSF.putString("lockKey", "0");
                        Constant.setSF.putString("sleepKeyCheckOFF", "sleepKeyCheckOFF");
                        Constant.setSF.apply();


                        Intent intent = new Intent(mContext, SplashScreenMy.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                        // need otp


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<change_numberModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });
    }

    public static void send_otpDelete(Context mContext, String mobileNoOld, String uid, EditText ninty1) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobileNoOld);

            Log.d(TAG, "@@@send_otp :" + SEND_OTP_COMMON + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP_COMMON, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mContext, otpverifyScreenDelete.class);
                                    intent.putExtra("phoneKy", mobileNoOld);
                                    intent.putExtra("uidKey", uid);
                                    intent.putExtra("country_codeKey", ninty1.getText().toString());
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void reSendOtpDelete(Context mContext, String mobileNoOld) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobileNoOld);

            Log.d(TAG, "@@@send_otp :" + SEND_OTP_COMMON + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP_COMMON, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void reSendOtpForget(Context mContext, String mobileNoOld, String uid, Dialog dialogLayoutColor) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobileNoOld);

            Log.d(TAG, "@@@send_otp :" + SEND_OTP_COMMON + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP_COMMON, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    dialogLayoutColor.dismiss();
                                    Constant.getSfFuncion(mContext);

                                    Intent intent = new Intent(mContext, forgetScreenOtp.class);
                                    intent.putExtra("phoneKy", mobileNoOld);
                                    intent.putExtra("uidKey", uid);
                                    intent.putExtra("country_codeKey", Constant.getSF.getString(Constant.COUNTRY_CODE, ""));
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void reSendOtpDeleteForgetOtp(Context mContext, String mobileNoOld) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("mobile_no", mobileNoOld);

            Log.d(TAG, "@@@send_otp :" + SEND_OTP_COMMON + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(SEND_OTP_COMMON, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                                } else {
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_emoji(Context mContext, chattingScreen chattingScreen) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            Log.d(TAG, "@@@fetch_emoji_data :" + BASE_URL + "emojiController/fetch_emoji_data");
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "emojiController/fetch_emoji_data", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@fetch_emoji_datacscsa :" + response.toString());

                            try {
                                JSONArray data = response.getJSONArray("data");


                                ArrayList<Emoji> emojis = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    String slug = obj.getString("slug");
                                    String character = obj.getString("character");
                                    String unicode_name = obj.getString("unicode_name");
                                    String code_point = obj.getString("code_point");
                                    String group = obj.getString("group");
                                    String sub_group = obj.getString("sub_group");
                                    emojis.add(new Emoji(slug, character, unicode_name, code_point, group, sub_group));

                                }

                                chattingScreen.setAdapteEMojir(emojis);
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void get_emojiAdd(Context mContext, RecyclerView emojiLongRec, ProgressBar progressBar, String modelId, String receiverUid) {
        try {
            progressBar.setVisibility(View.VISIBLE);

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            Log.d(TAG, "@@@fetch_emoji_data :" + BASE_URL + "emojiController/fetch_emoji_data");
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "emojiController/fetch_emoji_data", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@fetch_emoji_datacscsa :" + response.toString());
                            progressBar.setVisibility(View.GONE);

                            try {
                                JSONArray data = response.getJSONArray("data");


                                ArrayList<Emoji> emojis = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    String slug = obj.getString("slug");
                                    String character = obj.getString("character");
                                    String unicode_name = obj.getString("unicode_name");
                                    String code_point = obj.getString("code_point");
                                    String group = obj.getString("group");
                                    String sub_group = obj.getString("sub_group");
                                    emojis.add(new Emoji(slug, character, unicode_name, code_point, group, sub_group));

                                }


                                emoji_adapter_addbtn adapter = new emoji_adapter_addbtn(mContext, emojis, modelId, receiverUid);
                                emojiLongRec.setLayoutManager(new GridLayoutManager(mContext, 9));
                                emojiLongRec.setAdapter(adapter);

                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }


                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }


    public static void get_emojiChatadapter(Context mContext, RecyclerView emojiLongRec, emojiAdapterChatAdapter adapter, String modelId, String receiverUid, String emojiCount, ArrayList<emojiModel> emoji, messageModel model, String userFTokenKey) {

        ArrayList<Emoji> emojis = new ArrayList<>();

        // **Static emojis list (संपूर्ण डेटा कायम ठेवला आहे)**
        emojis.add(new Emoji("e0-6-thumbs-up", "👍", "E0.6 thumbs up", "1F44D", "people-body", "hand-fingers-closed"));
        emojis.add(new Emoji("e0-6-red-heart", "❤️", "E0.6 red heart", "2764 FE0F", "smileys-emotion", "heart"));
        emojis.add(new Emoji("e0-6-face-with-tears-of-joy", "😂", "E0.6 face with tears of joy", "1F602", "smileys-emotion", "face-smiling"));
        emojis.add(new Emoji("e0-6-face-with-open-mouth", "😮", "E0.6 face with open mouth", "1F62E", "smileys-emotion", "face-surprised"));
        emojis.add(new Emoji("e0-6-smiling-face-with-tear", "🥲", "E0.6 smiling face with tear", "1F972", "smileys-emotion", "face-smiling"));
        emojis.add(new Emoji("e0-6-folded-hands", "🙏", "E0.6 folded hands", "1F64F", "people-body", "hand-fingers-open"));
        emojis.add(new Emoji("e0-6-face-blowing-a-kiss", "😘", "E0.6 face blowing a kiss", "1F618", "smileys-emotion", "face-affection"));
        emojis.add(new Emoji("e0-6-smiling-face-with-hearts", "🥰", "E0.6 smiling face with hearts", "1F970", "smileys-emotion", "face-smiling"));
        emojis.add(new Emoji("e0-6-maple-leaf", "🍁", "E0.6 maple leaf", "1F341", "animals-nature", "plant-other"));
        emojis.add(new Emoji("e0-6-artist-palette", "🎨", "E0.6 artist palette", "1F3A8", "activities", "arts-crafts"));
        emojis.add(new Emoji("e0-6-long-drum", "🪘", "E0.6 long drum", "1FA98", "objects", "musical-instrument"));
        emojis.add(new Emoji("e0-6-pear", "🍐", "E0.6 pear", "1F350", "food-drink", "food-fruit"));

        // **नेहमी शेवटी रिकामे emoji ठेवणे**
        // emojis.add(new Emoji("e0-6-red-heart", "", "", "2764 FE0F", "smileys-emotion", "heart"));

        Constant.getSfFuncion(mContext);
        String senderUid = Constant.getSF.getString(Constant.UID_KEY, "");
        String receiverRoom = receiverUid + senderUid;

        DatabaseReference emojiRef2 = FirebaseDatabase.getInstance().getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).child("emojiModel");

        // **Adapter ला आधीच इनिशियलाइज़ करा**
        emojiAdapterChatAdapter adapterNew = new emojiAdapterChatAdapter(mContext, emojis, modelId, receiverUid, emojiCount, emoji, model, userFTokenKey);
        emojiLongRec.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        emojiLongRec.setAdapter(adapterNew);

        emojiRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashSet<String> emojiHashSet = new HashSet<>(); // **Duplicate टाळण्यासाठी HashSet वापरला आहे**

                // **स्टॅटिक डेटा HashSet मध्ये टाका**
                for (Emoji e : emojis) {
                    emojiHashSet.add(e.getCharacter());
                }

                // **Firebase मधून डेटा मिळवणे**
                for (DataSnapshot data : snapshot.getChildren()) {
                    String emoji = data.child("emoji").getValue(String.class);
                    String name = data.child("name").getValue(String.class);

                    if (emoji != null && name != null && !emojiHashSet.contains(emoji)) {
                        emojis.add(new Emoji("", emoji, name, "", "", ""));
                        emojiHashSet.add(emoji); // **HashSet मध्ये emoji जोडा**
                    }
                }

                // **नेहमी शेवटी रिकामे emoji ठेवा (तुमच्या request प्रमाणे)**
                if (!emojis.get(emojis.size() - 1).getCharacter().isEmpty()) {
                    emojis.add(new Emoji("e0-6-red-heart", "", "", "2764 FE0F", "smileys-emotion", "heart"));
                }

                // **RecyclerView अपडेट करा**
                adapterNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error fetching emojiModel: " + error.getMessage());
            }
        });
    }


    public static void get_emojiGrp(Context mContext, grpChattingScreen grpChattingScreen) {
        try {

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            Log.d(TAG, "@@@fetch_emoji_data :" + BASE_URL + "emojiController/fetch_emoji_data");
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "emojiController/fetch_emoji_data", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@fetch_emoji_datacscsa :" + response.toString());

                            try {
                                JSONArray data = response.getJSONArray("data");


                                ArrayList<Emoji> emojis = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    String slug = obj.getString("slug");
                                    String character = obj.getString("character");
                                    String unicode_name = obj.getString("unicode_name");
                                    String code_point = obj.getString("code_point");
                                    String group = obj.getString("group");
                                    String sub_group = obj.getString("sub_group");
                                    emojis.add(new Emoji(slug, character, unicode_name, code_point, group, sub_group));

                                }

                                grpChattingScreen.setAdapteEMojirrr(emojis);
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }
    }

    public static void delete_chatingindivisual(Context mContext, String modelId, String uid, String receiverUid) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("model_id", modelId);
            request_param.put("sender_id", uid);
            request_param.put("receiver_id", receiverUid);


            Log.d(TAG, "@@@send_otp :" + BASE_URL + "delete_chatingindivisual" + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_chatingindivisual", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response bnkjckwanbkawcnlwa:" + response.toString());

                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void delete_chatingindivisualReceiver(Context mContext, String model_id, String room, String sender_id, String receiver_id) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("model_id", model_id);
            request_param.put("room", room);
            request_param.put("sender_id", sender_id);
            request_param.put("receiver_id", receiver_id);


            Log.d(TAG, "@@@send_otp :" + BASE_URL + "delete_reciver_msg" + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_reciver_msg", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@send_otp_response bnkjckwanbkawcnlwa:" + response.toString());

                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void delete_groupp(Context mContext, String groupId, grpListAdapter grpListAdapter, int adapterPosition, Dialog dialogLayoutColor) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("group_id", groupId);
            //  Toast.makeText(mContext, groupId, Toast.LENGTH_SHORT).show();


            Log.d(TAG, "@@@delete_groupp :" + BASE_URL + "delete_groupp  " + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_groupp", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_grouppResponse bnkjckwanbkawcnlwa:" + response.toString());

                            String status = null;
                            try {
                                status = response.getString("error_code");
                                if (status.equals("200")) {
                                    try {
                                        new DatabaseHelper(mContext).deleteGroupById(groupId);
                                    } catch (Exception e) {

                                    }
                                    grpListAdapter.removeItem(adapterPosition);
                                    dialogLayoutColor.dismiss();
                                    Constant.getSfFuncion(mContext);
                                    String uid = Constant.getSF.getString(Constant.UID_KEY, "");


                                    FirebaseDatabase.getInstance().getReference().child("group_chats").child(uid + groupId).removeValue();


                                } else {

                                }

                            } catch (JSONException e) {

                            }

                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void delete_video_call_log(Context mContext, String id, childCallingLogAdapter childCallingLogAdapterVoice, int adapterPosition, Dialog dialogLayoutColor, String f_id, String call_type) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            Constant.getSfFuncion(mContext);
            request_param.put("uid", Constant.getSF.getString(Constant.UID_KEY, ""));
            request_param.put("f_id", f_id);
            request_param.put("call_type", call_type);


            Log.d(TAG, "@@@delete_groupp :" + BASE_URL + "delete_video_call_log     " + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_video_call_log", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_grouppResponse bnkjckwanbkawcnlwa:" + response.toString());

                            String status = null;
                            try {
                                status = response.getString("error_code");
                                if (status.equals("200")) {
                                    childCallingLogAdapterVoice.removeItem(adapterPosition);
                                    dialogLayoutColor.dismiss();
                                }

                            } catch (JSONException e) {

                            }

                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void delete_voice_call_log(Context mContext, String id, childCallingLogAdapterVoice childCallingLogAdapterVoice, int adapterPosition, Dialog dialogLayoutColor, String f_id, String call_type) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", Constant.getSF.getString(Constant.UID_KEY, ""));
            request_param.put("f_id", f_id);
            request_param.put("call_type", call_type);


            Log.d(TAG, "@@@delete_groupp :" + BASE_URL + "delete_voice_call_log   " + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_voice_call_log", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_grouppResponse bnkjckwanbkawcnlwa:" + response.toString());

                            String status = null;
                            try {
                                status = response.getString("error_code");
                                if (status.equals("200")) {

                                    childCallingLogAdapterVoice.removeItem(adapterPosition);
                                    dialogLayoutColor.dismiss();

                                } else {

                                }

                            } catch (JSONException e) {

                            }

                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void delete_sender_all_msg(Context mContext, String sender_id, String receiver_id, String room) {
        try {
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("sender_id", sender_id);
            request_param.put("receiver_id", receiver_id);
            request_param.put("room", room);

            Log.d(TAG, "@@@delete_groupp :" + BASE_URL + "delete_sender_all_msg   " + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(BASE_URL + "delete_sender_all_msg", request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@delete_sender_all_msg:" + response.toString());


                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get_group_details(forGroupVisible forGroupVisible, String groupId, ImageView profile, TextView pName, Context mContext) {
        Constant.getSfFuncion(mContext);
        String viewer_uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("group_id", groupId);
        builder.addFormDataPart("viewer_uid", viewer_uid);
        RequestBody requestBody = builder.build();

        api.get_group_details(requestBody).enqueue(new Callback<get_group_detailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<get_group_detailsResponseModel> call, @NonNull Response<get_group_detailsResponseModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.body().getData().getGroup_icon());

                if (response.body() != null) {
                    groupD data = response.body().getData();
                    pName.setText(data.getGroup_name());

                    safeLoadImage(data.getGroup_icon(), profile, R.drawable.inviteimg);

                    profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, show_image_Screen.class);
                            intent.putExtra("imageKey", data.getGroup_icon());
                                                SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                        }
                    });

                    forGroupVisible.setAdapter(data.getMembers());


                } else {
                    Log.d(TAG, "onResponseCntact: " + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<get_group_detailsResponseModel> call, @NonNull Throwable t) {
                Log.e("loginerror", t.getMessage());
            }
        });
    }

    // New method to get group members for adapter use
    public static void get_group_members_for_adapter(String groupId, Context mContext, GroupMembersCallback callback) {
        Constant.getSfFuncion(mContext);
        String viewer_uid = Constant.getSF.getString(Constant.UID_KEY, "");
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("group_id", groupId);
        builder.addFormDataPart("viewer_uid", viewer_uid);
        RequestBody requestBody = builder.build();

        api.get_group_details(requestBody).enqueue(new Callback<get_group_detailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<get_group_detailsResponseModel> call, @NonNull Response<get_group_detailsResponseModel> response) {
                if (response.body() != null && response.body().getData() != null) {
                    groupD data = response.body().getData();
                    if (data.getMembers() != null) {
                        callback.onMembersReceived(data.getMembers());
                    } else {
                        callback.onMembersReceived(new ArrayList<>());
                    }
                } else {
                    callback.onMembersReceived(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<get_group_detailsResponseModel> call, @NonNull Throwable t) {
                Log.e("GroupMembers", "Failed to get group members: " + t.getMessage());
                callback.onMembersReceived(new ArrayList<>());
            }
        });
    }

    // Callback interface for group members
    public interface GroupMembersCallback {
        void onMembersReceived(ArrayList<members> members);
    }

    public static class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {

                }
            }
        }
    }

    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }


    public static void create_individual_chattingSharedContact(Context mContext, String senderId, String receiverUid, String messages, File upload_docs, String dataType, String extension, String name, String phone, String profilepic, String miceTiming, String sent_time, String senderRoom, String receiverRoom, messageModel model, String modelId, String user_name, FirebaseDatabase database, String userFTokenKey, Activity mActivity, int notification, File savedThumbnail, Dialog globalDialoue, String device_type, CardView customToastCard, TextView customToastText, int i, ArrayList<forwardnameModel> receivedNameList, ProgressBar progressbar) {
        try {
            SecretKey key;

            progressbar.setVisibility(View.VISIBLE);

            int count = 0;
            Constant.setSfFunction(mContext);
            Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
            Constant.setSF.apply();

            Log.d("apihittime", String.valueOf(count + 1));
            Log.d("captiondta", model.getCaption());

            client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            try {
                request_param.put("uid", senderId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("friend_id", receiverUid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("message", messages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("user_name", user_name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //always keep 1
            request_param.put("notification", String.valueOf(1));
            if (upload_docs == null) {
                request_param.put("upload_docs", "");
            } else {

                try {
                    request_param.put("upload_docs", upload_docs);
                } catch (Exception ex) {
                    // Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            try {
                request_param.put("dataType", dataType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("dataType", dataType);
            try {
                request_param.put("extension", extension);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("name", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("phone", phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("micPhoto", profilepic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                request_param.put("miceTiming", miceTiming);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("sent_time", sent_time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                request_param.put("model_id", model.getModelId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("idddd", senderId + ":" + receiverUid + ":" + messages);
            Log.d(TAG, "@@@create_individual_chatting :" + CREATE_INDIVIDUAL_CHATTING + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {

                    client.post(CREATE_INDIVIDUAL_CHATTING, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@create_individual_chatting_response :" + response.toString());

                            try {
                                int status = response.getInt("error_code");
                                if (status == 200) {

                                    JSONObject data = response.getJSONObject("data");

                                    String total_msg_limit = data.getString("total_msg_limit");
                                    String user_name = data.getString("user_name");

                                    if (!total_msg_limit.equals("0")) {
                                        globalDialoue.dismiss();

                                        Constant.showCustomToast("( " + user_name + " )" + " Msg limit set for a privacy in a day - " + total_msg_limit, customToastCard, customToastText);
                                    } else {
                                        //todo can  eligible to send


                                        Constant.setSfFunction(mContext);
                                        Constant.setSF.putString(Constant.startMsgKey, Constant.startMsgKey);
                                        Constant.setSF.apply();


                                        Log.d("apihittime", String.valueOf(count + 1));
                                        Log.d("captiondta", model.getCaption());

                                        database.getReference().child(Constant.CHAT).child(senderRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                database.getReference().child(Constant.CHAT).child(receiverRoom).child(modelId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        //   Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();

                                                        TextView percentage = globalDialoue.findViewById(R.id.percentage);
                                                        percentage.setText("100 %");

                                                        CircularProgressIndicator progressBar = globalDialoue.findViewById(R.id.progressbar);
                                                        if (progressBar != null) {
                                                            progressBar.setProgress(100);
                                                        }


                                                        if (userFTokenKey != null) {

                                                            Constant.getSfFuncion(mContext);
                                                            String sleepKey = Constant.getSF.getString(Constant.sleepKey, "");


                                                            if (sleepKey.equals(Constant.sleepKey)) {

                                                            } else {

                                                                Log.d(TAG, "cabcjkqbkca: " + model.getDataType());

                                                                String fcm = "";
                                                                try {
                                                                    if (model.getDataType().equals(Constant.Text)) {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), model.getMessage(), senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());

                                                                    } else if (model.getDataType().equals(Constant.img)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have a new Image", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());

                                                                    } else if (model.getDataType().equals(Constant.contact)) {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have a new Contact", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else if (model.getDataType().equals(Constant.voiceAudio)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a Audio", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else if (model.getDataType().equals(Constant.video)) {
                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a Video", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());


                                                                    } else {

                                                                        Webservice.end_notification_api(mContext, userFTokenKey, model.getUserName(), "You have new a File", senderId, user_name, Constant.getSF.getString(Constant.profilePic, ""), sent_time, device_type, model.getUid(), model.getMessage(), model.getTime(), model.getDocument(), model.getDataType(), model.getExtension(), model.getName(), model.getPhone(), model.getMicPhoto(), model.getMiceTiming(), model.getUserName(), model.getReplytextData(), model.getReplyKey(), model.getReplyType(), model.getReplyOldData(), model.getReplyCrtPostion(), model.getModelId(), model.getReceiverUid(), model.getForwaredKey(), model.getGroupName(), model.getDocSize(), model.getFileName(), model.getThumbnail(), model.getFileNameThumbnail(), model.getCaption(), model.getNotification(), model.getCurrentDate(), model.getSelectionCount());
                                                                    }
                                                                } catch (Exception e) {
                                                                    //  throw new RuntimeException(e);
                                                                }
                                                            }

                                                        }


                                                        String pushKey = database.getReference().child(Constant.chattingSocket).child(receiverUid).push().getKey();

                                                        database.getReference().child(Constant.chattingSocket).child(receiverUid).setValue(pushKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                            }
                                                        });


                                                        if (i == receivedNameList.size() - 1) {
                                                            progressbar.setVisibility(View.GONE);
                                                            Intent intent = new Intent(mContext, MainActivityOld.class);
                                                            intent.putExtra("sentfromExternal", "sentfromExternal");
                                                            intent.putExtra("receiverUidsentfromExternal", receiverUid);
                                                            SwipeNavigationHelper.startActivityWithSwipe((Activity) mContext, intent);
                                                        }

                                                    }
                                                });
                                            }
                                        });

                                    }

                                } else if (status == 403) {
                                    String message = response.getString("message");
                                    View parentLayout = ((Activity) mContext).findViewById(android.R.id.content);
                                    Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    }).setActionTextColor(mContext.getResources().getColor(android.R.color.holo_red_light)).show();

                                } else {
                                    String message = response.getString("message");
                                    // Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            //
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                            //


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void cancelAllRequests() {

        client.cancelAllRequests(true);
    }


    public static void getDataFromAPIInitial(Context mContext, String uids, inviteScreen inviteScreen, ProgressBar progressBarMain, LinearLayout clearcalllog, CardView valuable, TextView textCard, long page_no) {
        try {
            final AsyncHttpClient client = new AsyncHttpClient();
            client.setConnectTimeout(30000); // 30 seconds
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uids);
            request_param.put("page_no", page_no);
            Log.d(TAG, "@@@get_users_all_contact :" + GET_USERS_ALL_CONTACT + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(GET_USERS_ALL_CONTACT, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_users_all_contact_response :" + response.toString());

                            try {
                                String status = response.getString("error_code");
                                if (status.equals("200")) {
                                    String message = response.getString("message");
                                    inviteScreen.isLoading = false;


                                    JSONArray data = response.getJSONArray("data");

                                    ArrayList<allContactListModel> allContactListModelList = new ArrayList<>();

                                    //      int inviteShow = 0;
                                    for (int i = 0; i < data.length(); i++) {

                                        String c_flag = data.getJSONObject(i).getString("c_flag");
                                        if (c_flag.equals("1")) {
                                            // TODO: 19/07/24 active users

                                            String uid = data.getJSONObject(i).getString("uid");
                                            String photo = data.getJSONObject(i).getString("photo");
                                            String full_name = data.getJSONObject(i).getString("full_name");
                                            String mobile_no = data.getJSONObject(i).getString("mobile_no");
                                            String caption = data.getJSONObject(i).getString("caption");
                                            String f_token = data.getJSONObject(i).getString("f_token");
                                            String device_type = data.getJSONObject(i).getString("device_type");
                                            boolean block = data.getJSONObject(i).getBoolean("block");
                                            boolean iamblocked = data.getJSONObject(i).getBoolean("iamblocked");
                                            allContactListModelList.add(new allContactListModel(c_flag, uid, photo, full_name, mobile_no, caption, f_token, device_type, "", "", block, iamblocked));

                                            try {
                                                new DatabaseHelper(mContext).insert_get_users_all_contactTableInviteSpecial(mContext, c_flag, uid, photo, full_name, mobile_no, caption, f_token, device_type, "", "", block, iamblocked);
                                            } catch (Exception e) {

                                            }

                                        } else {

                                            // TODO: 19/07/24 invite users
                                            String contact_name = data.getJSONObject(i).getString("contact_name");
                                            String contact_number = data.getJSONObject(i).getString("contact_number");
                                            allContactListModelList.add(new allContactListModel(c_flag, "", "", "", "", "", "", "", contact_name, contact_number, false, false));


                                            try {
                                                new DatabaseHelper(mContext).insert_get_users_all_contactTableInviteSpecial(mContext, c_flag, "", "", "", "", "", "", "", contact_name, contact_number, false, false);
                                            } catch (Exception e) {

                                            }

                                        }

                                    }

                                    inviteScreen.setAdapter(allContactListModelList);

                                    progressBarMain.setVisibility(View.GONE);

                                    if (allContactListModelList.size() == 0) {
                                        valuable.setVisibility(View.VISIBLE);
                                        textCard.setText("No contacts available");
                                    } else {
                                        valuable.setVisibility(View.GONE);
                                    }


                                } else {
                                    //  progressBarMain.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "getDataFromAPIInitial: " + e.getMessage());
        }

    }

    public static void getDataFromAPIInitialLoadMore(
            Context mContext,
            String uids,
            com.Appzia.enclosure.Screens.inviteScreen inviteScreen,
            ProgressBar progressBarMain,
            LinearLayout clearcalllog,
            CardView valuable,
            TextView textCard,
            long page_no,
            ProgressBar progressbar,
            PaginationCallback callback
    ) {
        try {
            final AsyncHttpClient client = new AsyncHttpClient();
            client.setConnectTimeout(30000);
            client.setResponseTimeout(30000);
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uids);
            request_param.put("page_no", page_no);

            Log.d(TAG, "@@@get_users_all_contact_loadMore: " + request_param.toString());
            Log.d(TAG, "@@@get_users_all_contact URL: " + GET_USERS_ALL_CONTACT + "?" + request_param.toString());

            ((Activity) mContext).runOnUiThread(() ->
                    client.post(GET_USERS_ALL_CONTACT, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "loadMore onSuccess(JSONObject): statusCode=" + statusCode + " body=" + response);
                            try {
                                String status = response.optString("error_code", "");
                                Log.d(TAG, "loadMore error_code=" + status + " page_no=" + page_no);

                                if ("200".equals(status)) {
                                    inviteScreen.isLoading = false;
                                    progressbar.setVisibility(View.GONE);

                                    JSONArray data = response.optJSONArray("data");
                                    ArrayList<allContactListModel> allContactListModelList = new ArrayList<>();

                                    if (data != null) {
                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject obj = data.optJSONObject(i);
                                            if (obj == null) continue;

                                            String c_flag = obj.optString("c_flag", "0");

                                            // Flexible boolean parsing for "block" / "iamblocked"
                                            boolean block = false;
                                            if (obj.has("block")) {
                                                Object b = obj.opt("block");
                                                if (b instanceof Boolean) {
                                                    block = (Boolean) b;
                                                } else if (b != null) {
                                                    String bs = b.toString();
                                                    block = "1".equals(bs) || "true".equalsIgnoreCase(bs);
                                                }
                                            }
                                            boolean iamblocked = false;
                                            if (obj.has("iamblocked")) {
                                                Object ib = obj.opt("iamblocked");
                                                if (ib instanceof Boolean) {
                                                    iamblocked = (Boolean) ib;
                                                } else if (ib != null) {
                                                    String ibs = ib.toString();
                                                    iamblocked = "1".equals(ibs) || "true".equalsIgnoreCase(ibs);
                                                }
                                            }

                                            if ("1".equals(c_flag)) {
                                                allContactListModelList.add(new allContactListModel(
                                                        c_flag,
                                                        obj.optString("uid", ""),
                                                        obj.optString("photo", ""),
                                                        obj.optString("full_name", ""),
                                                        obj.optString("mobile_no", ""),
                                                        obj.optString("caption", ""),
                                                        obj.optString("f_token", ""),
                                                        obj.optString("device_type", ""),
                                                        "", // contact_name
                                                        "", // contact_number
                                                        block,
                                                        iamblocked
                                                ));
                                                try {
                                                    new DatabaseHelper(mContext).insert_get_users_all_contactTableInviteSpecial(
                                                            mContext,
                                                            c_flag,
                                                            obj.optString("uid", ""),
                                                            obj.optString("photo", ""),
                                                            obj.optString("full_name", ""),
                                                            obj.optString("mobile_no", ""),
                                                            obj.optString("caption", ""),
                                                            obj.optString("f_token", ""),
                                                            obj.optString("device_type", ""),
                                                            "",
                                                            "",
                                                            block,
                                                            iamblocked
                                                    );
                                                } catch (Exception ignored) {}
                                            } else {
                                                allContactListModelList.add(new allContactListModel(
                                                        c_flag,
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        obj.optString("contact_name", ""),
                                                        obj.optString("contact_number", ""),
                                                        block,
                                                        iamblocked
                                                ));
                                                try {
                                                    new DatabaseHelper(mContext).insert_get_users_all_contactTableInviteSpecial(
                                                            mContext,
                                                            c_flag,
                                                            "",
                                                            "",
                                                            "",
                                                            "",
                                                            "",
                                                            "",
                                                            "",
                                                            obj.optString("contact_name", ""),
                                                            obj.optString("contact_number", ""),
                                                            block,
                                                            iamblocked
                                                    );
                                                } catch (Exception ignored) {}
                                            }
                                        }
                                    }

                                    inviteScreen.setAdapterNotifiyInserted(allContactListModelList);
                                    progressBarMain.setVisibility(View.GONE);

                                    if (allContactListModelList.isEmpty()) {
                                        valuable.setVisibility(View.VISIBLE);
                                        textCard.setText("No contacts available");
                                    } else {
                                        valuable.setVisibility(View.GONE);
                                    }

                                    Log.d(TAG, "loadMore success; items=" + allContactListModelList.size() + " page_no=" + page_no);
                                    callback.onPageLoadSuccess();
                                    String msg = response.optString("message", "Loaded");
                                 //   Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                                } else {
                                    String message = response.optString("message", "Failed");
                                    Log.w(TAG, "loadMore non-200: status=" + status + " msg=" + message + " page_no=" + page_no);
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    callback.onPageLoadFailure();
                                }

                            } catch (Exception e) {
                                Log.e(TAG, "loadMore parse error: " + e.getMessage());
                                callback.onPageLoadFailure();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d(TAG, "loadMore onSuccess(JSONArray): statusCode=" + statusCode + " body=" + response);
                            callback.onPageLoadFailure();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.d(TAG, "loadMore onSuccess(String): statusCode=" + statusCode + " body=" + responseString);
                            callback.onPageLoadFailure();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e(TAG, "loadMore onFailure(str): statusCode=" + statusCode
                                    + " error=" + (throwable != null ? throwable.getMessage() : "null")
                                    + " resp=" + responseString);
                            callback.onPageLoadFailure();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e(TAG, "loadMore onFailure(json): statusCode=" + statusCode
                                    + " error=" + (throwable != null ? throwable.getMessage() : "null")
                                    + " resp=" + (errorResponse != null ? errorResponse.toString() : "null"));
                            callback.onPageLoadFailure();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.e(TAG, "loadMore onFailure(array): statusCode=" + statusCode
                                    + " error=" + (throwable != null ? throwable.getMessage() : "null")
                                    + " resp=" + (errorResponse != null ? errorResponse.toString() : "null"));
                            callback.onPageLoadFailure();
                        }
                    })
            );
        } catch (Exception e) {
            Log.w(TAG, "getDataFromAPIInitialLoadMore exception: " + e.getMessage());
            callback.onPageLoadFailure();
        }
    }

    public static void get_country_list(Context mContext, flagScreen flagscreen, ProgressBar progressBar) {
        Retrofit retrofit = APIClient.getClient();
        API api = retrofit.create(API.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        api.get_country_list().enqueue(new Callback<flagNewModel>() {
            @Override
            public void onResponse(@NonNull Call<flagNewModel> call, @NonNull Response<flagNewModel> response) {
                Log.d(TAG, "onResponseupload_user_contact_listRetrofit: " + response.body().getError_code());

                if (response.body() != null) {

                    String errorcode = response.body().getError_code();
                    String message = response.body().getMessage();
                    ArrayList<flagNewModelChild> data = response.body().getData();

                    if (errorcode.equals("200")) {
                        progressBar.setVisibility(View.GONE);

                        flagscreen.setAdaper(data);


                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + response.toString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<flagNewModel> call, @NonNull Throwable t) {
                //   Toast.makeText(mContext, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("loginerror", t.getMessage());
            }
        });
    }

    public static void search_from_all_contact(Context mContext, String uid, String dataSearch, ProgressBar progressBarMain, inviteScreen inviteScreen, LinearLayout clearcalllog, CardView valuable, TextView textCard) {
        try {
            progressBarMain.setVisibility(View.VISIBLE);
            final AsyncHttpClient client = new AsyncHttpClient();
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            final RequestParams request_param = new RequestParams();
            request_param.put("uid", uid);
            if (dataSearch.isEmpty()) {
                request_param.put("srch_keyword", "");
            } else {
                request_param.put("srch_keyword", dataSearch);
            }
            Log.d(TAG, "@@@get_users_all_contact :" + search_from_all_contact + "?" + request_param.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                public void run() {
                    client.post(search_from_all_contact, request_param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(TAG, "@@@get_users_all_contact_response :" + response.toString());

                            try {
                                String status = response.getString("error_code");
                                if (status.equals("200")) {
                                    String message = response.getString("message");


                                    JSONArray data = response.getJSONArray("data");

                                    ArrayList<allContactListModel> allContactListModelList = new ArrayList<>();


                                    for (int i = 0; i < data.length(); i++) {

                                        String c_flag = data.getJSONObject(i).getString("c_flag");
                                        if (c_flag.equals("1")) {
                                            // TODO: 19/07/24 active users

                                            String uid = data.getJSONObject(i).getString("uid");
                                            String photo = data.getJSONObject(i).getString("photo");
                                            String full_name = data.getJSONObject(i).getString("full_name");
                                            String mobile_no = data.getJSONObject(i).getString("mobile_no");
                                            String caption = data.getJSONObject(i).getString("caption");
                                            String f_token = data.getJSONObject(i).getString("f_token");
                                            String device_type = data.getJSONObject(i).getString("device_type");
                                            boolean block = data.getJSONObject(i).getBoolean("block");
                                            boolean iamblocked = data.getJSONObject(i).getBoolean("iamblocked");
                                            allContactListModelList.add(new allContactListModel(c_flag, uid, photo, full_name, mobile_no, caption, f_token, device_type, "", "", block, iamblocked));


                                        } else {

                                            // TODO: 19/07/24 invite users
                                            String contact_name = data.getJSONObject(i).getString("contact_name");
                                            String contact_number = data.getJSONObject(i).getString("contact_number");
                                            allContactListModelList.add(new allContactListModel(c_flag, "", "", "", "", "", "", "", contact_name, contact_number, false, false));
                                        }
                                    }

                                    inviteScreen.setAdapter(allContactListModelList);

                                    progressBarMain.setVisibility(View.GONE);

                                    if (allContactListModelList.size() == 0) {
                                        valuable.setVisibility(View.VISIBLE);
                                        textCard.setText("No contacts available");
                                    } else {
                                        valuable.setVisibility(View.GONE);
                                    }


                                } else {
                                    //  progressBarMain.setVisibility(View.GONE);
                                    String message = response.getString("message");
                                    //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    //    Webservice.getDataFromAPIInitial(mContext, uid, inviteScreen, progressBarMain, clearcalllog, valuable, textCard);
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();

                                Log.d("@@@ notSuccess: ", e.getMessage());
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {


                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        } catch (Exception e) {

            Log.w(TAG, "getDataFromAPIInitial: " + e.getMessage());

        }
    }


    public static String truncateToWordss(String message, int letterLimit) {
        // Debug: Check input
        System.out.println("Input message: '" + message + "'");
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Input is null or empty, returning empty string.");
            return "";
        }

        // Split by whitespace (handles multiple spaces, tabs, etc.)
        String[] words = message.trim().split("\\s+");
        System.out.println("Number of words: " + words.length);

        StringBuilder result = new StringBuilder();
        int letterCount = 0;

        // Iterate through words
        for (String word : words) {
            // Count non-whitespace characters in the current word
            int wordLetterCount = word.replaceAll("\\s", "").length();
            System.out.println("Word: '" + word + "', Letter count: " + wordLetterCount);

            // Check if adding this word exceeds the letter limit
            if (letterCount + wordLetterCount > letterLimit) {
                System.out.println("Stopping at letter count: " + letterCount);
                break; // Stop if adding the word exceeds the limit
            }

            // Add the word and a space
            result.append(word).append(" ");
            letterCount += wordLetterCount;
        }

        // Return the truncated string, removing trailing space
        String finalResult = result.toString().trim();
        System.out.println("Final output: '" + finalResult + "'");
        System.out.println("Final letter count: " + finalResult.replaceAll("\\s", "").length());
        return finalResult;
    }


    public interface Callback2 {
        void onSuccess(JSONObject response);

        void onFailure(Exception e);
    }
}

