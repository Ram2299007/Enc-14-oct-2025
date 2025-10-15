package com.Appzia.enclosure.Utils.OfflineDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Appzia.enclosure.Model.Emoji;
import com.Appzia.enclosure.Model.allContactListModel;
import com.Appzia.enclosure.Model.call_log_history_model;
import com.Appzia.enclosure.Model.callingUserInfoChildModel;
import com.Appzia.enclosure.Model.callloglistModel;
import com.Appzia.enclosure.Model.emojiModel;
import com.Appzia.enclosure.Model.get_calling_contact_list_model;
import com.Appzia.enclosure.Model.get_contact_invite_model;
import com.Appzia.enclosure.Model.get_contact_model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_MessageLmt_Model;
import com.Appzia.enclosure.Model.get_user_active_contact_list_Model;
import com.Appzia.enclosure.Model.group_messageModel;
import com.Appzia.enclosure.Model.group_messageModel2;
import com.Appzia.enclosure.Model.grp_list_child2_model;
import com.Appzia.enclosure.Model.grp_list_child_model;
import com.Appzia.enclosure.Model.linkPreviewModel;
import com.Appzia.enclosure.Model.messageModel;
import com.Appzia.enclosure.Model.messagemodel2;
import com.Appzia.enclosure.Model.profileDBModel;
import com.Appzia.enclosure.Model.profilestatusModel;
import com.Appzia.enclosure.Model.selectionBunchModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "enclosureDatabase.db";
    public static final int DATABASE_VERSION = 11;
    public static final String uid = "uid";
    public static final String photo = "photo";
    public static final String full_name = "full_name";
    public static final String mobile_no = "mobile_no";
    public static final String start_time = "start_time";
    public static final String caption = "caption";
    public static final String f_token = "f_token";
    public static final String contact_number = "contact_number";
    public static final String contact_name = "contact_name";
    public static final String msg_limit = "msg_limit";
    //invite
    public static final String device_type = "device_type";
    public static final String original_name = "original_name";
    public static final String block = "block";
    public static final String iamblocked = "iamblocked";
    public static final String room = "room";
    public static final String message_id = "message_id";
    public static final String message = "message";
    public static final String dataType = "dataType";
    public static final String sent_time = "sent_time";
    public static final String notification = "notification";
    public static final String currentDate = "currentDate";
    public static final String timestamp = "timestamp";
    public static final String created_at = "created_at";
    public static final String id = "id";
    public static final String slug = "slug";
    public static final String character = "character";
    public static final String unicodeName = "unicodeName";
    public static final String codePoint = "codePoint";
    public static final String groups = "groups";
    public static final String subGroup = "subGroup";

    public static final String date = "date";
    public static final String end_time = "end_time";
    public static final String calling_flag = "calling_flag";
    public static final String call_type = "call_type";

    //TABLE NAME
    public static final String get_user_active_chat_list = "get_user_active_chat_list";
    public static final String chatsTable = "chats";
    public static final String group_chatsTable = "group_chats";
    public static final String get_group_listTable = "get_group_list";
    public static final String grp_list_child2_modelTable = "grp_list_child2_model";
    public static final String get_profileTable = "get_profile";
    public static final String get_user_profile_imagesTable = "get_user_profile_images";
    public static final String get_calling_contact_listTable = "get_calling_contact_list";

    // todo voice call
    public static final String get_voice_call_logTable = "get_voice_call_log";
    public static final String get_voice_call_logChild1Table = "get_voice_call_logChild1";
    public static final String get_voice_call_logChild2Table = "get_voice_call_logChild2";


    // todo video call

    public static final String get_voice_call_logVIDEOTable = "get_voice_call_logVIDEO";
    public static final String get_voice_call_logChild1VIDEOTable = "get_voice_call_logChild1VIDEO";
    public static final String get_voice_call_logChild2VIDEOTable = "get_voice_call_logChild2VIDEO";
    public static final String get_users_all_contactTable = "get_users_all_contact";
    public static final String get_users_all_contactTableInviteSpecial = "get_users_all_contactTableInviteSpecial";
    public static final String get_users_all_contactCHILDTable = "get_users_all_contactCHILD";
    public static final String linkPreviewTable = "linkPreviewTable";
    public static final String fetch_emoji_dataTable = "fetch_emoji_dataTable";

    String TAG = "DATABASE_HELPER";


    public static final String time = "time";
    public static final String document = "document";
    public static final String extension = "extension";
    public static final String name = "name";
    public static final String phone = "phone";
    public static final String miceTiming = "miceTiming";
    public static final String micPhoto = "micPhoto";
    public static final String createdBy = "createdBy";
    public static final String userName = "userName";
    public static final String replytextData = "replytextData";
    public static final String replyKey = "replyKey";
    public static final String replyType = "replyType";
    public static final String replyOldData = "replyOldData";
    public static final String replyCrtPostion = "replyCrtPostion";
    public static final String modelId = "modelId";
    public static final String title = "title";
    public static final String description = "description";
    public static final String url = "url";
    public static final String favIcon = "favIcon";
    public static final String image_url = "image_url";
    public static final String receiverUid = "receiverUid";
    public static final String forwaredKey = "forwaredKey";
    public static final String groupName = "groupName";
    public static final String docSize = "docSize";
    public static final String fileName = "fileName";
    public static final String thumbnail = "thumbnail";
    public static final String group_icon = "group_icon";
    public static final String sr_nos = "sr_nos";
    public static final String group_name = "group_name";
    public static final String group_id = "group_id";
    public static final String friend_id = "friend_id";
    public static final String group_created_by = "group_created_by";
    public static final String fileNameThumbnail = "fileNameThumbnail";
    public static final String group_members_count = "group_members_count";
    public static final String sentTime = "sentTime";
    public static final String dec_flg = "dec_flg";
    public static final String l_msg = "l_msg";
    public static final String data_type = "data_type";
    public static final String chatsTableId = "chatsTableId";
    public static final String group_chatsTableId = "group_chatsTableId";
    public static final String c_flag = "c_flag";


    public static final String TABLE_NAME = "messages";
    public static final String TABLE_NAME_GROUP = "messages_group";
    public static final String TABLE_NAME_PENDING = "pending_messages";
    public static final String TABLE_NAME_GROUP_PENDING = "group_pending_messages";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                "uid TEXT," +
                "message TEXT," +
                "time TEXT," +
                "document TEXT," +
                "dataType TEXT," +
                "extension TEXT," +
                "name TEXT," +
                "phone TEXT," +
                "micPhoto TEXT," +
                "miceTiming TEXT," +
                "userName TEXT," +
                "replytextData TEXT," +
                "replyKey TEXT," +
                "replyType TEXT," +
                "replyOldData TEXT," +
                "replyCrtPostion TEXT," +
                "modelId TEXT," +
                "receiverUid TEXT," +
                "forwaredKey TEXT," +
                "groupName TEXT," +
                "docSize TEXT," +
                "fileName TEXT," +
                "thumbnail TEXT," +
                "fileNameThumbnail TEXT," +
                "caption TEXT," +
                "notification INTEGER," +
                "currentDate TEXT," +
                "emojiModel TEXT," +
                "emojiCount TEXT," +
                "imageWidthDp TEXT," +
                "imageHeightDp TEXT," +
                "aspectRatio TEXT," +
                "timestamp INTEGER," +
                "active INTEGER," +
                "PRIMARY KEY (modelId, receiverUid)" +
                ")";
        try {
            db.execSQL(CREATE_TABLE);
            Log.d(TAG, "Table " + TABLE_NAME + " created successfully");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating table: " + e.getMessage(), e);
        }


        String CREATE_TABLE_group = "CREATE TABLE " + TABLE_NAME_GROUP + " (" +
                "uid TEXT," +
                "message TEXT," +
                "time TEXT," +
                "document TEXT," +
                "dataType TEXT," +
                "extension TEXT," +
                "name TEXT," +
                "phone TEXT," +
                "micPhoto TEXT," +
                "miceTiming TEXT," +
                "createdBy TEXT," +
                "userName TEXT," +
                "modelId TEXT," +
                "receiverUid TEXT," +
                "docSize TEXT," +
                "fileName TEXT," +
                "thumbnail TEXT," +
                "fileNameThumbnail TEXT," +
                "caption TEXT," +
                "currentDate TEXT," +
                "imageWidthDp TEXT," +
                "imageHeightDp TEXT," +
                "aspectRatio TEXT," +
                "senderRoom TEXT," +
                "active INTEGER," +
                "PRIMARY KEY (modelId, senderRoom)" +
                ")";

        try {
            db.execSQL(CREATE_TABLE_group);
            Log.d(TAG, "Table " + TABLE_NAME_GROUP + " created successfully");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating table: " + e.getMessage(), e);
        }

        // Create pending messages table for messages being uploaded
        String CREATE_TABLE_PENDING = "CREATE TABLE " + TABLE_NAME_PENDING + " (" +
                "uid TEXT," +
                "message TEXT," +
                "time TEXT," +
                "document TEXT," +
                "dataType TEXT," +
                "extension TEXT," +
                "name TEXT," +
                "phone TEXT," +
                "micPhoto TEXT," +
                "miceTiming TEXT," +
                "userName TEXT," +
                "replytextData TEXT," +
                "replyKey TEXT," +
                "replyType TEXT," +
                "replyOldData TEXT," +
                "replyCrtPostion TEXT," +
                "modelId TEXT," +
                "receiverUid TEXT," +
                "forwaredKey TEXT," +
                "groupName TEXT," +
                "docSize TEXT," +
                "fileName TEXT," +
                "thumbnail TEXT," +
                "fileNameThumbnail TEXT," +
                "caption TEXT," +
                "notification INTEGER," +
                "currentDate TEXT," +
                "emojiModel TEXT," +
                "emojiCount TEXT," +
                "imageWidthDp TEXT," +
                "imageHeightDp TEXT," +
                "aspectRatio TEXT," +
                "timestamp INTEGER," +
                "selectionCount TEXT," +
                "selectionBunch TEXT," +
                "uploadStatus INTEGER DEFAULT 0," +
                "PRIMARY KEY (modelId, receiverUid)" +
                ")";

        try {
            db.execSQL(CREATE_TABLE_PENDING);
            Log.d(TAG, "Table " + TABLE_NAME_PENDING + " created successfully");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating pending table: " + e.getMessage(), e);
        }

        // Create group pending messages table for group messages being uploaded
        String CREATE_TABLE_GROUP_PENDING = "CREATE TABLE " + TABLE_NAME_GROUP_PENDING + " (" +
                "uid TEXT," +
                "message TEXT," +
                "time TEXT," +
                "document TEXT," +
                "dataType TEXT," +
                "extension TEXT," +
                "name TEXT," +
                "phone TEXT," +
                "micPhoto TEXT," +
                "miceTiming TEXT," +
                "userName TEXT," +
                "replytextData TEXT," +
                "replyKey TEXT," +
                "replyType TEXT," +
                "replyOldData TEXT," +
                "replyCrtPostion TEXT," +
                "modelId TEXT," +
                "grpIdKey TEXT," +
                "forwaredKey TEXT," +
                "groupName TEXT," +
                "docSize TEXT," +
                "fileName TEXT," +
                "thumbnail TEXT," +
                "fileNameThumbnail TEXT," +
                "caption TEXT," +
                "notification INTEGER," +
                "currentDate TEXT," +
                "emojiModel TEXT," +
                "emojiCount TEXT," +
                "imageWidthDp TEXT," +
                "imageHeightDp TEXT," +
                "aspectRatio TEXT," +
                "timestamp INTEGER," +
                "selectionCount TEXT," +
                "selectionBunch TEXT," +
                "uploadStatus INTEGER DEFAULT 0," +
                "PRIMARY KEY (modelId, grpIdKey)" +
                ")";

        try {
            db.execSQL(CREATE_TABLE_GROUP_PENDING);
            Log.d(TAG, "Table " + TABLE_NAME_GROUP_PENDING + " created successfully");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating group pending table: " + e.getMessage(), e);
        }

        try {
            String get_user_active_chat_listStatement = "CREATE TABLE IF NOT EXISTS " + get_user_active_chat_list + "(" +
                    uid + " TEXT NOT NULL PRIMARY KEY UNIQUE, " +
                    "" + photo + " TEXT, "
                    + full_name + " BLOB, "
                    + room + " TEXT, "
                    + mobile_no + " TEXT, "
                    + caption + " BLOB, " + f_token + " TEXT, " + msg_limit + " TEXT, " + message_id + " TEXT, " + message + " BLOB, " + dataType + " TEXT, " + sent_time + " TEXT, " + notification + " INTEGER, "
                    + created_at + " TEXT, "
                    + device_type + " TEXT, "
                    + original_name + " TEXT, "
                    + block + " INTEGER DEFAULT 0, "
                    + iamblocked + " INTEGER DEFAULT 0 "
                    + ")";

            db.execSQL(get_user_active_chat_listStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String chatsStatement = "CREATE TABLE IF NOT EXISTS " + chatsTable + " ("
                    + modelId + " TEXT PRIMARY KEY NOT NULL UNIQUE, "
                    + uid + " TEXT, "
                    + message + " BLOB, "
                    + time + " TEXT, "
                    + document + " TEXT, "
                    + dataType + " TEXT, "
                    + extension + " TEXT, "
                    + name + " TEXT, "
                    + phone + " TEXT, "
                    + micPhoto + " TEXT, "
                    + miceTiming + " TEXT, "
                    + userName + " TEXT, "
                    + replytextData + " BLOB, "
                    + replyKey + " TEXT, "
                    + replyType + " TEXT, "
                    + replyOldData + " BLOB, "
                    + replyCrtPostion + " TEXT, "
                    + receiverUid + " TEXT, "
                    + forwaredKey + " TEXT, "
                    + groupName + " TEXT, "
                    + docSize + " TEXT, "
                    + fileName + " TEXT, "
                    + thumbnail + " TEXT, "
                    + fileNameThumbnail + " TEXT, "
                    + caption + " TEXT, "
                    + notification + " INTEGER, "
                    + currentDate + " TEXT,"
                    + timestamp + " INTEGER"
                    + ")";
            db.execSQL(chatsStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String group_chatsStatement = "CREATE TABLE IF NOT EXISTS " + group_chatsTable + "(" + modelId + " TEXT PRIMARY KEY NOT NULL UNIQUE, " + group_chatsTableId + " TEXT NOT NULL, " + uid + " TEXT, " + message + " BLOB, " + time + " TEXT, " + document + " TEXT, " + dataType + " TEXT, " + extension + " TEXT, " + name + " TEXT, " + phone + " TEXT, " + miceTiming + " TEXT, " + micPhoto + " TEXT, " + createdBy + " TEXT, " + userName + " TEXT, " + receiverUid + " TEXT, " + docSize + " TEXT, " + fileName + " TEXT, " + thumbnail + " TEXT, " + fileNameThumbnail + " TEXT, " + caption + " TEXT " + ")";
            db.execSQL(group_chatsStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_group_listStatement = "CREATE TABLE IF NOT EXISTS " + get_group_listTable + "(" + group_id + " TEXT PRIMARY KEY NOT NULL UNIQUE, " + sr_nos + " INTEGER , " + group_name + " BLOB , " + group_icon + " TEXT, "
                    + group_created_by + " BLOB, "
                    + sentTime + " TEXT, "
                    + dec_flg + " TEXT, "
                    + l_msg + " TEXT, " +
                    data_type + " TEXT, " +
                    f_token + " TEXT, " + group_members_count + " TEXT " + ")";
            db.execSQL(get_group_listStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String grp_list_child2_modelStatement = "CREATE TABLE IF NOT EXISTS " + grp_list_child2_modelTable + "(" +
                    group_id + " TEXT, " +
                    friend_id + " TEXT, " +
                    f_token + " TEXT, " +
                    device_type + " TEXT, " +
                    "PRIMARY KEY (" + group_id + ", " + friend_id + ")" + ")";
            db.execSQL(grp_list_child2_modelStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_profileTableStatement = "CREATE TABLE IF NOT EXISTS " + get_profileTable + "(" + uid + " TEXT PRIMARY KEY NOT NULL, " + mobile_no + " TEXT , " + full_name + " BLOB, " + caption + " BLOB, " + photo + " TEXT, " + f_token + " TEXT " + ")";

            db.execSQL(get_profileTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_user_profile_imagesTableStatement = "CREATE TABLE IF NOT EXISTS " + get_user_profile_imagesTable + "(" + uid + " TEXT , " + id + " TEXT PRIMARY KEY NOT NULL, " + photo + " TEXT, " + f_token + " TEXT " + ")";

            db.execSQL(get_user_profile_imagesTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_calling_contact_listTableStatement = "CREATE TABLE IF NOT EXISTS " + get_calling_contact_listTable + "(" + uid + " TEXT PRIMARY KEY NOT NULL, " + photo + " TEXT, " + full_name + " BLOB, " + mobile_no + " TEXT, " + caption + " BLOB, "
                    + f_token + " TEXT, " + device_type + " TEXT " + ")";

            db.execSQL(get_calling_contact_listTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        // TODO VOICE CALL LOGS
        try {
            String get_voice_call_logTableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logTable + "(" +
                    date + " TEXT PRIMARY KEY NOT NULL, " +
                    sr_nos + " INTEGER " + ")";

            db.execSQL(get_voice_call_logTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_voice_call_logChild1TableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logChild1Table + "(" +
                    id + " TEXT PRIMARY KEY NOT NULL, "
                    + date + " TEXT, " +
                    friend_id + " TEXT, "
                    + photo + " TEXT, "
                    + full_name + " BLOB, "
                    + f_token + " TEXT, "
                    + end_time + " TEXT, "
                    + calling_flag + " TEXT, "
                    + call_type + " TEXT, "
                    + mobile_no + " TEXT, "
                    + start_time + " TEXT, "
                    + device_type + " TEXT " + ")";
            db.execSQL(get_voice_call_logChild1TableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_voice_call_logChild2TableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logChild2Table + "(" +
                    id + " TEXT PRIMARY KEY NOT NULL, "
                    + uid + " TEXT, "
                    + friend_id + " TEXT, "
                    + date + " TEXT, "
                    + start_time + " BLOB, "
                    + end_time + " TEXT, "
                    + calling_flag + " TEXT, "
                    + call_type + " TEXT " + ")";
            db.execSQL(get_voice_call_logChild2TableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        //TODO VIDEO CALL LOGS

        try {
            String get_voice_call_logVIDEOTableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logVIDEOTable + "(" +
                    date + " TEXT PRIMARY KEY NOT NULL, " +
                    sr_nos + " INTEGER " + ")";

            db.execSQL(get_voice_call_logVIDEOTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_voice_call_logChild1VIDEOTableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logChild1VIDEOTable + "(" +
                    id + " TEXT PRIMARY KEY NOT NULL, "
                    + date + " TEXT, " +
                    friend_id + " TEXT, "
                    + photo + " TEXT, "
                    + full_name + " BLOB, "
                    + f_token + " TEXT, "
                    + end_time + " TEXT, "
                    + calling_flag + " TEXT, "
                    + call_type + " TEXT, "
                    + mobile_no + " TEXT, "
                    + start_time + " TEXT, " + device_type + " TEXT " + ")";
            db.execSQL(get_voice_call_logChild1VIDEOTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }

        try {
            String get_voice_call_logChild2VIDEOTableStatement = "CREATE TABLE IF NOT EXISTS " + get_voice_call_logChild2VIDEOTable + "(" +
                    id + " TEXT PRIMARY KEY NOT NULL, "
                    + uid + " TEXT, "
                    + friend_id + " TEXT, "
                    + date + " TEXT, "
                    + start_time + " BLOB, "
                    + end_time + " TEXT, "
                    + calling_flag + " TEXT, "
                    + call_type + " TEXT " + ")";
            db.execSQL(get_voice_call_logChild2VIDEOTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_users_all_contactStatement = "CREATE TABLE IF NOT EXISTS " + get_users_all_contactTable + "(" +
                    uid + " TEXT PRIMARY KEY NOT NULL, "
                    + photo + " TEXT, "
                    + full_name + " BLOB, "
                    + mobile_no + " TEXT, "
                    + caption + " BLOB, "
                    + f_token + " TEXT, "
                    + block + " INTEGER DEFAULT 0, "
                    + device_type + " TEXT " + ")";
            db.execSQL(get_users_all_contactStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }
        try {
            String get_users_all_contactTableInviteSpecialStatement = "CREATE TABLE IF NOT EXISTS " + get_users_all_contactTableInviteSpecial + "(" +
                    uid + " TEXT, "
                    + photo + " TEXT, "
                    + full_name + " BLOB, "
                    + mobile_no + " TEXT, "
                    + caption + " BLOB, "
                    + f_token + " TEXT, "
                    + c_flag + " TEXT, "
                    + contact_name + " TEXT, "
                    + contact_number + " TEXT, "
                    + block + " INTEGER DEFAULT 0, "
                    + iamblocked + " INTEGER DEFAULT 0, "
                    + device_type + " TEXT " + ")";
            db.execSQL(get_users_all_contactTableInviteSpecialStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String get_users_all_contactCHILDTableStatement = "CREATE TABLE IF NOT EXISTS " + get_users_all_contactCHILDTable + "(" +
                    contact_number + " TEXT PRIMARY KEY NOT NULL, "
                    + contact_name + " BLOB, "
                    + c_flag + " Text " +

                    ")";
            db.execSQL(get_users_all_contactCHILDTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        // todo preview link offline
        try {
            String linkPreviewTableStatement = "CREATE TABLE IF NOT EXISTS " + linkPreviewTable + "(" +
                    url + " BLOB  , "
                    + modelId + " TEXT PRIMARY KEY NOT NULL, "
                    + title + " BLOB, "
                    + description + " BLOB, "
                    + favIcon + " TEXT, "
                    + image_url + " TEXT "
                    + ")";
            db.execSQL(linkPreviewTableStatement);
        } catch (Exception ignored) {
            Log.d("TAG", "onCreate: " + ignored.getMessage());
        }


        try {
            String fetch_emoji_dataTableStatement = "CREATE TABLE IF NOT EXISTS `" + fetch_emoji_dataTable + "` (" +
                    "`" + slug + "` TEXT, " +
                    "`" + character + "` TEXT, " +
                    "`" + unicodeName + "` TEXT, " +
                    "`" + codePoint + "` BLOB NOT NULL PRIMARY KEY UNIQUE, " +
                    "`" + groups + "` TEXT, " +
                    "`" + subGroup + "` TEXT" + ")";
            db.execSQL(fetch_emoji_dataTableStatement);
        } catch (Exception e) {
            Log.e("TAG", "onCreate: " + e.getMessage(), e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle specific version upgrades
        if (oldVersion < 10) {
            // Create pending messages table for version 10
            try {
                String CREATE_TABLE_PENDING = "CREATE TABLE " + TABLE_NAME_PENDING + " (" +
                        "uid TEXT," +
                        "message TEXT," +
                        "time TEXT," +
                        "document TEXT," +
                        "dataType TEXT," +
                        "extension TEXT," +
                        "name TEXT," +
                        "phone TEXT," +
                        "micPhoto TEXT," +
                        "miceTiming TEXT," +
                        "userName TEXT," +
                        "replytextData TEXT," +
                        "replyKey TEXT," +
                        "replyType TEXT," +
                        "replyOldData TEXT," +
                        "replyCrtPostion TEXT," +
                        "modelId TEXT," +
                        "receiverUid TEXT," +
                        "forwaredKey TEXT," +
                        "groupName TEXT," +
                        "docSize TEXT," +
                        "fileName TEXT," +
                        "thumbnail TEXT," +
                        "fileNameThumbnail TEXT," +
                        "caption TEXT," +
                        "notification INTEGER," +
                        "currentDate TEXT," +
                        "emojiModel TEXT," +
                        "emojiCount TEXT," +
                        "imageWidthDp TEXT," +
                        "imageHeightDp TEXT," +
                        "aspectRatio TEXT," +
                        "timestamp INTEGER," +
                        "selectionCount TEXT," +
                        "selectionBunch TEXT," +
                        "uploadStatus INTEGER DEFAULT 0," +
                        "PRIMARY KEY (modelId, receiverUid)" +
                        ")";
                db.execSQL(CREATE_TABLE_PENDING);
                Log.d(TAG, "Created pending messages table in upgrade");
            } catch (Exception e) {
                Log.e(TAG, "Error creating pending table in upgrade: " + e.getMessage());
            }
        }

        if (oldVersion < 11) {
            // Create group pending messages table for version 11
            try {
                String CREATE_TABLE_GROUP_PENDING = "CREATE TABLE " + TABLE_NAME_GROUP_PENDING + " (" +
                        "uid TEXT," +
                        "message TEXT," +
                        "time TEXT," +
                        "document TEXT," +
                        "dataType TEXT," +
                        "extension TEXT," +
                        "name TEXT," +
                        "phone TEXT," +
                        "micPhoto TEXT," +
                        "miceTiming TEXT," +
                        "userName TEXT," +
                        "replytextData TEXT," +
                        "replyKey TEXT," +
                        "replyType TEXT," +
                        "replyOldData TEXT," +
                        "replyCrtPostion TEXT," +
                        "modelId TEXT," +
                        "grpIdKey TEXT," +
                        "forwaredKey TEXT," +
                        "groupName TEXT," +
                        "docSize TEXT," +
                        "fileName TEXT," +
                        "thumbnail TEXT," +
                        "fileNameThumbnail TEXT," +
                        "caption TEXT," +
                        "notification INTEGER," +
                        "currentDate TEXT," +
                        "emojiModel TEXT," +
                        "emojiCount TEXT," +
                        "imageWidthDp TEXT," +
                        "imageHeightDp TEXT," +
                        "aspectRatio TEXT," +
                        "timestamp INTEGER," +
                        "selectionCount TEXT," +
                        "selectionBunch TEXT," +
                        "uploadStatus INTEGER DEFAULT 0," +
                        "PRIMARY KEY (modelId, grpIdKey)" +
                        ")";
                db.execSQL(CREATE_TABLE_GROUP_PENDING);
                Log.d(TAG, "Created group pending messages table in upgrade");
            } catch (Exception e) {
                Log.e(TAG, "Error creating group pending messages table in upgrade: " + e.getMessage());
            }
        }
        
        if (oldVersion < 9) {
            // Add iamblocked column to existing table if upgrading from version 8 or earlier
            try {
                db.execSQL("ALTER TABLE " + get_users_all_contactTableInviteSpecial + " ADD COLUMN " + iamblocked + " INTEGER DEFAULT 0");
                Log.d(TAG, "Added iamblocked column to " + get_users_all_contactTableInviteSpecial);
            } catch (Exception e) {
                Log.e(TAG, "Error adding iamblocked column: " + e.getMessage());
                // If ALTER fails, recreate the table
                db.execSQL("DROP TABLE IF EXISTS " + get_users_all_contactTableInviteSpecial);
                String get_users_all_contactTableInviteSpecialStatement = "CREATE TABLE IF NOT EXISTS " + get_users_all_contactTableInviteSpecial + "(" +
                        uid + " TEXT, "
                        + photo + " TEXT, "
                        + full_name + " BLOB, "
                        + mobile_no + " TEXT, "
                        + caption + " BLOB, "
                        + f_token + " TEXT, "
                        + c_flag + " TEXT, "
                        + contact_name + " TEXT, "
                        + contact_number + " TEXT, "
                        + block + " INTEGER DEFAULT 0, "
                        + iamblocked + " INTEGER DEFAULT 0, "
                        + device_type + " TEXT " + ")";
                db.execSQL(get_users_all_contactTableInviteSpecialStatement);
            }
        }

        // For major version changes, drop and recreate all tables
        if (newVersion > oldVersion + 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PENDING);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP_PENDING);
            db.execSQL("DROP TABLE IF EXISTS " + get_user_active_chat_list);
            db.execSQL("DROP TABLE IF EXISTS " + chatsTable);
            db.execSQL("DROP TABLE IF EXISTS " + group_chatsTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_group_listTable);
            db.execSQL("DROP TABLE IF EXISTS " + grp_list_child2_modelTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_profileTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_user_profile_imagesTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_calling_contact_listTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logChild1Table);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logChild2Table);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logVIDEOTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logChild1VIDEOTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_voice_call_logChild2VIDEOTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_users_all_contactTable);
            db.execSQL("DROP TABLE IF EXISTS " + get_users_all_contactTableInviteSpecial);
            db.execSQL("DROP TABLE IF EXISTS " + get_users_all_contactCHILDTable);
            db.execSQL("DROP TABLE IF EXISTS " + linkPreviewTable);
            db.execSQL("DROP TABLE IF EXISTS " + fetch_emoji_dataTable);
            onCreate(db);
        }
    }

    //TODO : - get_user_active_chat_list
    public void insert_get_user_active_chat_list(Context mContext, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String message, String dataType, String sent_time, int notification, String msg_limit, String device_type, String original_name, boolean block, boolean iamblocked) {

        int blockValue = 0;
        if (block) {
            blockValue = 1;
        } else {
            blockValue = 0;
        }

        int iamblockedValue = 0;
        if (iamblocked) {
            iamblockedValue = 1;
        } else {
            iamblockedValue = 0;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.message, message);
        values.put(DatabaseHelper.dataType, dataType);
        values.put(DatabaseHelper.sent_time, sent_time);
        values.put(DatabaseHelper.notification, notification);
        values.put(DatabaseHelper.msg_limit, msg_limit);
        values.put(DatabaseHelper.device_type, device_type);
        values.put(DatabaseHelper.original_name, original_name);
        values.put(DatabaseHelper.block, blockValue);
        values.put(DatabaseHelper.iamblocked, iamblockedValue);
        long result = db.insertWithOnConflict(DatabaseHelper.get_user_active_chat_list, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_user_active_chat_list: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_user_active_chat_list: " + "success");
        }


    }


    //TODO : - get_user_active_chat_list
    public void insert_getEmoji(Context mContext, String slug, String character, String unicodeName, String codePoint, String groups, String subGroup) {
        SQLiteDatabase db = this.getWritableDatabase(); // Ensure the database is writable

        // Insert the data into a ContentValues object
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.slug, slug);
        values.put(DatabaseHelper.character, character);
        values.put(DatabaseHelper.unicodeName, unicodeName);
        values.put(DatabaseHelper.codePoint, codePoint);  // Assuming codePoint is a string. If it's numeric, change accordingly
        values.put(DatabaseHelper.groups, groups);
        values.put(DatabaseHelper.subGroup, subGroup);

        // Insert or replace data into the fetch_emoji_dataTable
        long result = db.insertWithOnConflict(DatabaseHelper.fetch_emoji_dataTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            Log.d("TAG", "insert_getEmoji: failed");
        } else {
            Log.d("TAG", "insert_getEmoji: success");
        }
    }


    //TODO : - chatsTable

    /// 2024
    public void insert_chatsTable(Context mContext, String uid, String message, String time, String document, String dataType, String extension, String name, String phone, String micPhoto, String miceTiming, String userName, String replytextData, String replyKey, String replyType, String replyOldData, String replyCrtPostion, String modelId, String receiverUid, String forwaredKey, String groupName, String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption, int notification, String currentDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.message, message);
        values.put(DatabaseHelper.time, time);
        values.put(DatabaseHelper.document, document);
        values.put(DatabaseHelper.dataType, dataType);
        values.put(DatabaseHelper.extension, extension);
        values.put(DatabaseHelper.name, name);
        values.put(DatabaseHelper.phone, phone);
        values.put(DatabaseHelper.micPhoto, micPhoto);
        values.put(DatabaseHelper.miceTiming, miceTiming);
        values.put(DatabaseHelper.userName, userName);
        values.put(DatabaseHelper.replytextData, replytextData);
        values.put(DatabaseHelper.replyKey, replyKey);
        values.put(DatabaseHelper.replyType, replyType);
        values.put(DatabaseHelper.replyOldData, replyOldData);
        values.put(DatabaseHelper.replyCrtPostion, replyCrtPostion);
        values.put(DatabaseHelper.modelId, modelId);
        values.put(DatabaseHelper.receiverUid, receiverUid);
        values.put(DatabaseHelper.forwaredKey, forwaredKey);
        values.put(DatabaseHelper.groupName, groupName);
        values.put(DatabaseHelper.docSize, docSize);
        values.put(DatabaseHelper.fileName, fileName);
        values.put(DatabaseHelper.thumbnail, thumbnail);
        values.put(DatabaseHelper.fileNameThumbnail, fileNameThumbnail);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.notification, notification);
        values.put(DatabaseHelper.currentDate, currentDate);


        long result = db.insertWithOnConflict(DatabaseHelper.chatsTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_chatsTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_chatsTable: " + "success");
        }


    }


    // here we need to fetch chatting

    //TODO : - chatsTable
//    public ArrayList<messageModel> getOldDataChatsTable(String receiverUids, String currentModelId) {
//        ArrayList<messageModel> dataList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Fetch messages where receiverUid matches, modelId is less than the currentModelId, limited to 15 results
//        Cursor cursor = db.rawQuery(
//                "SELECT * FROM " + chatsTable +
//                        " WHERE receiverUid = ? AND modelId < ? ORDER BY modelId ASC LIMIT 100",
//                new String[]{receiverUids, currentModelId}
//        );
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                // Print cursor content for debugging
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    String columnName = cursor.getColumnName(i);
//                    String columnValue = cursor.getString(i);
//                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
//                }
//
//                int uidIndex = cursor.getColumnIndexOrThrow(uid);
//                int messageIndex = cursor.getColumnIndexOrThrow(message);
//                int timeIndex = cursor.getColumnIndexOrThrow(time);
//                int documentIndex = cursor.getColumnIndexOrThrow(document);
//                int dataTypeIndex = cursor.getColumnIndexOrThrow(dataType);
//                int extensionIndex = cursor.getColumnIndexOrThrow(extension);
//                int nameIndex = cursor.getColumnIndexOrThrow(name);
//                int phoneIndex = cursor.getColumnIndexOrThrow(phone);
//                int micPhotoIndex = cursor.getColumnIndexOrThrow(micPhoto);
//                int miceTimingIndex = cursor.getColumnIndexOrThrow(miceTiming);
//                int userNameIndex = cursor.getColumnIndexOrThrow(userName);
//                int replytextDataIndex = cursor.getColumnIndexOrThrow(replytextData);
//                int replyKeyIndex = cursor.getColumnIndexOrThrow(replyKey);
//                int replyTypeIndex = cursor.getColumnIndexOrThrow(replyType);
//                int replyOldDataIndex = cursor.getColumnIndexOrThrow(replyOldData);
//                int replyCrtPostionIndex = cursor.getColumnIndexOrThrow(replyCrtPostion);
//                int modelIdIndex = cursor.getColumnIndexOrThrow(modelId);
//                int receiverUidIndex = cursor.getColumnIndexOrThrow(receiverUid);
//                int forwaredKeyIndex = cursor.getColumnIndexOrThrow(forwaredKey);
//                int groupNameIndex = cursor.getColumnIndexOrThrow(groupName);
//                int docSizeIndex = cursor.getColumnIndexOrThrow(docSize);
//                int fileNameIndex = cursor.getColumnIndexOrThrow(fileName);
//                int thumbnailIndex = cursor.getColumnIndexOrThrow(thumbnail);
//                int fileNameThumbnailIndex = cursor.getColumnIndexOrThrow(fileNameThumbnail);
//                int captionIndex = cursor.getColumnIndexOrThrow(caption);
//                int notificationIndex = cursor.getColumnIndexOrThrow(notification);
//                int currentDateIndex = cursor.getColumnIndexOrThrow(currentDate);
//                int timestampIndex = cursor.getColumnIndexOrThrow(timestamp);
//
//                String uid = cursor.getString(uidIndex);
//                String message = cursor.getString(messageIndex);
//                String time = cursor.getString(timeIndex);
//                String document = cursor.getString(documentIndex);
//                String dataType = cursor.getString(dataTypeIndex);
//                String extension = cursor.getString(extensionIndex);
//                String name = cursor.getString(nameIndex);
//                String phone = cursor.getString(phoneIndex);
//                String micPhoto = cursor.getString(micPhotoIndex);
//                String miceTiming = cursor.getString(miceTimingIndex);
//                String userName = cursor.getString(userNameIndex);
//                String replytextData = cursor.getString(replytextDataIndex);
//                String replyKey = cursor.getString(replyKeyIndex);
//                String replyType = cursor.getString(replyTypeIndex);
//                String replyOldData = cursor.getString(replyOldDataIndex);
//                String replyCrtPostion = cursor.getString(replyCrtPostionIndex);
//                String modelId = cursor.getString(modelIdIndex);
//                String receiverUid = cursor.getString(receiverUidIndex);
//                String forwaredKey = cursor.getString(forwaredKeyIndex);
//                String groupName = cursor.getString(groupNameIndex);
//                String docSize = cursor.getString(docSizeIndex);
//                String fileName = cursor.getString(fileNameIndex);
//                String thumbnail = cursor.getString(thumbnailIndex);
//                String fileNameThumbnail = cursor.getString(fileNameThumbnailIndex);
//                String caption = cursor.getString(captionIndex);
//                int notification = cursor.getInt(notificationIndex);
//                String currentDate = cursor.getString(currentDateIndex);
//                String timestamp = cursor.getString(timestampIndex);
//
//                ArrayList<emojiModel>emojiModels = new ArrayList<>();
//                emojiModels.add(new emojiModel("",""));
//                messageModel dataModel = new messageModel(uid, message, time, document, dataType, extension, name, phone, micPhoto, miceTiming, userName, replytextData, replyKey, replyType, replyOldData, replyCrtPostion, modelId, receiverUid, forwaredKey, groupName, docSize, fileName, thumbnail, fileNameThumbnail, caption, notification, currentDate, emojiModels, "",timestamp);
//                dataList.add(dataModel);
//            }
//            cursor.close();
//        }
//
//        db.close();
//        return dataList;
//    }


    //TODO : - groupChatsTable
    public void insert_groupChatsTable(Context mContext, String uid, String message, String time, String document, String dataType, String extension, String name, String phone, String miceTiming, String micPhoto, String createdBy, String userName, String modelId, String group_chatsTableId, String receiverUid, String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.message, message);
        values.put(DatabaseHelper.time, time);
        values.put(DatabaseHelper.document, document);
        values.put(DatabaseHelper.dataType, dataType);
        values.put(DatabaseHelper.extension, extension);
        values.put(DatabaseHelper.name, name);
        values.put(DatabaseHelper.phone, phone);
        values.put(DatabaseHelper.miceTiming, miceTiming);
        values.put(DatabaseHelper.micPhoto, micPhoto);
        values.put(DatabaseHelper.createdBy, createdBy);
        values.put(DatabaseHelper.userName, userName);
        values.put(DatabaseHelper.modelId, modelId);
        values.put(DatabaseHelper.group_chatsTableId, group_chatsTableId);
        values.put(DatabaseHelper.receiverUid, receiverUid);
        values.put(DatabaseHelper.docSize, docSize);
        values.put(DatabaseHelper.fileName, fileName);
        values.put(DatabaseHelper.thumbnail, thumbnail);
        values.put(DatabaseHelper.fileNameThumbnail, fileNameThumbnail);
        values.put(DatabaseHelper.caption, caption);

        long result = db.insertWithOnConflict(DatabaseHelper.group_chatsTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_group_chatsTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_group_chatsTable: " + "success");
        }


    }


    //TODO : - get_group_listTable
    public void insert_get_group_listTable(Context mContext, String group_id, int sr_nos, String group_name, String group_icon, String group_created_by, String f_token, String group_members_count, ArrayList<grp_list_child2_model> group_members, String sentTime, String dec_flg, String l_msg) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.group_id, group_id);
        values.put(DatabaseHelper.sr_nos, sr_nos);
        values.put(DatabaseHelper.group_name, group_name);
        values.put(DatabaseHelper.group_icon, group_icon);
        values.put(DatabaseHelper.group_created_by, group_created_by);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.group_members_count, group_members_count);
        values.put(DatabaseHelper.sentTime, sentTime);
        values.put(DatabaseHelper.dec_flg, dec_flg);
        values.put(DatabaseHelper.l_msg, l_msg);

        db.insertWithOnConflict(DatabaseHelper.get_group_listTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);


        for (grp_list_child2_model model : group_members) {
            SQLiteDatabase db2 = this.getWritableDatabase();
            ContentValues memberValue = new ContentValues();
            memberValue.put(DatabaseHelper.group_id, group_id);
            memberValue.put(DatabaseHelper.friend_id, model.getFriend_id());
            memberValue.put(DatabaseHelper.f_token, model.getF_token());
            memberValue.put(DatabaseHelper.device_type, model.getDevice_type());
            long result = db2.insertWithOnConflict(DatabaseHelper.grp_list_child2_modelTable, null, memberValue, SQLiteDatabase.CONFLICT_IGNORE);

            if (result == -1) {
                // Toast.makeText(mContext, "faied", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void deleteGroupById(String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete from group_members table first (if dependent)
        db.delete(DatabaseHelper.grp_list_child2_modelTable,
                DatabaseHelper.group_id + " = ?",
                new String[]{group_id});

        // Then delete from main group table
        db.delete(DatabaseHelper.get_group_listTable,
                DatabaseHelper.group_id + " = ?",
                new String[]{group_id});

        db.close();
    }


    //TODO : - get_profileTable
    public void insert_get_profileTable(Context mContext, String uid, String full_name, String caption, String mobile_no, String photo, String f_token) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.f_token, f_token);

        long result = db.insertWithOnConflict(DatabaseHelper.get_profileTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_user_active_chat_list


    //TODO : - get_profileTable
    public void insert_get_user_profile_imagesTable(Context mContext, String uid, String id, String photo, String f_token) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.id, id);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.f_token, f_token);

        long result = db.insertWithOnConflict(DatabaseHelper.get_user_profile_imagesTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - linkPreviewTable
    public void insert_linkPreviewTable(Context mContext, String modelId, String url, String title, String description, String favIcon, String image_url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.modelId, modelId);
        values.put(DatabaseHelper.url, url);
        values.put(DatabaseHelper.title, title);
        values.put(DatabaseHelper.description, description);
        values.put(DatabaseHelper.favIcon, favIcon);
        values.put(DatabaseHelper.image_url, image_url);

        long result = db.insertWithOnConflict(DatabaseHelper.linkPreviewTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_calling_contact_listTable
    public void insert_get_calling_contact_listTable(Context mContext, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.device_type, device_type);

        long result = db.insertWithOnConflict(DatabaseHelper.get_calling_contact_listTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_voice_call_logTable
    public void insert_get_voice_call_logTable(Context mContext, String date, int sr_nos) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.sr_nos, sr_nos);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_voice_call_logChild1Table
    public void insert_gget_voice_call_logChild1Table(Context mContext, String id, String date, String friend_id, String photo, String full_name, String f_token, String end_time, String calling_flag, String call_type, String mobile_no, String start_time, String device_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.id, id);
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.friend_id, friend_id);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.end_time, end_time);
        values.put(DatabaseHelper.calling_flag, calling_flag);
        values.put(DatabaseHelper.call_type, call_type);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.start_time, start_time);
        values.put(DatabaseHelper.device_type, device_type);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logChild1Table, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_voice_call_logChild2Table
    public void insert_gget_voice_call_logChild2Table(Context mContext, String id, String uid, String friend_id, String date, String start_time, String end_time, String calling_flag, String call_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.id, id);
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.friend_id, friend_id);
        values.put(DatabaseHelper.end_time, end_time);
        values.put(DatabaseHelper.calling_flag, calling_flag);
        values.put(DatabaseHelper.call_type, call_type);
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.start_time, start_time);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logChild2Table, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "success");
        }

    }


    //TODO : - get_voice_call_logVIDEOTable
    public void insert_get_voice_call_logVIDEOTable(Context mContext, String date, int sr_nos) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.sr_nos, sr_nos);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logVIDEOTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_voice_call_logChild1VIDEOTable
    public void insert_gget_voice_call_logChild1VIDEOTable(Context mContext, String id, String date, String friend_id, String photo, String full_name, String f_token, String end_time, String calling_flag, String call_type, String mobile_no, String start_time, String device_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.id, id);
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.friend_id, friend_id);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.end_time, end_time);
        values.put(DatabaseHelper.calling_flag, calling_flag);
        values.put(DatabaseHelper.call_type, call_type);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.start_time, start_time);
        values.put(DatabaseHelper.device_type, device_type);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logChild1VIDEOTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_get_profileTable: " + "success");
        }

    }


    //TODO : - get_voice_call_logChild2VIDEOTable
    public void insert_gget_voice_call_logChild2VIDEOTable(Context mContext, String id, String uid, String friend_id, String date, String start_time, String end_time, String calling_flag, String call_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.id, id);
        values.put(DatabaseHelper.date, date);
        values.put(DatabaseHelper.friend_id, friend_id);
        values.put(DatabaseHelper.end_time, end_time);
        values.put(DatabaseHelper.calling_flag, calling_flag);
        values.put(DatabaseHelper.call_type, call_type);
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.start_time, start_time);

        long result = db.insertWithOnConflict(DatabaseHelper.get_voice_call_logChild2VIDEOTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            // Insertion failed, handle error
            //            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "failed");
        } else {
            // Insertion successful
            //  Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "success");
        }

    }


    //TODO : - get_users_all_contactTableTable
    public void insert_get_users_all_contactTable(Context mContext, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type, boolean block) {

        int blockValue = 0;
        if (block) {
            blockValue = 1;
        } else {
            blockValue = 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.device_type, device_type);
        values.put(DatabaseHelper.block, blockValue);

        db.beginTransaction();
        try {
            // Insert or update operation
            long result = db.insertWithOnConflict(DatabaseHelper.get_users_all_contactTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            if (result == -1) {
                Log.d("TAG", "insertOrUpdateUserContact: failed");
            } else {
                Log.d("TAG", "insertOrUpdateUserContact: success");
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("TAG", "Error while trying to insert or update user contact", e);
        } finally {
            db.endTransaction();
        }

    }


    //TODO : - get_users_all_contactTableTable
    public void insert_get_users_all_contactTableInviteSpecial(Context mContext, String c_flag, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type, String contact_name, String contact_number, boolean block,boolean iamblocked) {

        int blockInt = 0;
        if (block) {
            blockInt = 1;
        } else {
            blockInt = 0;
        }
        int iamblockedInt = 0;
        if (iamblocked) {
            iamblockedInt = 1;
        } else {
            iamblockedInt = 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.uid, uid);
        values.put(DatabaseHelper.photo, photo);
        values.put(DatabaseHelper.full_name, full_name);
        values.put(DatabaseHelper.mobile_no, mobile_no);
        values.put(DatabaseHelper.caption, caption);
        values.put(DatabaseHelper.f_token, f_token);
        values.put(DatabaseHelper.device_type, device_type);
        values.put(DatabaseHelper.c_flag, c_flag);
        values.put(DatabaseHelper.contact_name, contact_name);
        values.put(DatabaseHelper.contact_number, contact_number);
        values.put(DatabaseHelper.block, blockInt);
        values.put(DatabaseHelper.iamblocked, iamblockedInt);

        db.beginTransaction();
        try {
            // Insert or update operation
            long result = db.insertWithOnConflict(DatabaseHelper.get_users_all_contactTableInviteSpecial, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            if (result == -1) {
                Log.d("TAG", "insertOrUpdateUserContact: failed");
            } else {
                Log.d("TAG", "insertOrUpdateUserContact: success");
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("TAG", "Error while trying to insert or update user contact", e);
        } finally {
            db.endTransaction();
        }

    }

    //TODO : - get_users_all_contactTableTable
    public void insert_get_users_all_contactCHILDTableInviteSpecial(Context mContext, String c_flag, String contact_number, String contact_name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.contact_number, contact_number);
        values.put(DatabaseHelper.contact_name, contact_name);
        values.put(DatabaseHelper.c_flag, c_flag);

        long result = db.insertWithOnConflict(DatabaseHelper.get_users_all_contactCHILDTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (result == -1) {
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "failed");
        } else {
            Log.d("TAG", "insert_gget_voice_call_logChild2Table: " + "success");
        }

    }


//    public void insert_get_users_all_contactTableInvite(Context mContext, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type, String contact_name, String contact_number) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.uid, uid);
//        values.put(DatabaseHelper.photo, photo);
//        values.put(DatabaseHelper.full_name, full_name);
//        values.put(DatabaseHelper.mobile_no, mobile_no);
//        values.put(DatabaseHelper.caption, caption);
//        values.put(DatabaseHelper.f_token, f_token);
//        values.put(DatabaseHelper.device_type, device_type);
//
//        db.beginTransaction();
//        try {
//            // Insert or update operation
//            long result = db.insertWithOnConflict(DatabaseHelper.get_users_all_contactTable, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//
//            if (result == -1) {
//                Log.d("TAG", "insertOrUpdateUserContact: failed");
//            } else {
//                Log.d("TAG", "insertOrUpdateUserContact: success");
//            }
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("TAG", "Error while trying to insert or update user contact", e);
//        } finally {
//            db.endTransaction();
//        }
//
//    }


    //TODO : - get_user_active_chat_list
    public ArrayList<get_user_active_contact_list_Model> getAllData() {
        ArrayList<get_user_active_contact_list_Model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_user_active_chat_list, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int sent_timeIndex = cursor.getColumnIndexOrThrow(sent_time);
                int dataTypeIndex = cursor.getColumnIndexOrThrow(dataType);
                int messagesIndex = cursor.getColumnIndexOrThrow(message);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);
                int notificationIndex = cursor.getColumnIndexOrThrow(notification);
                int msg_limitIndex = cursor.getColumnIndexOrThrow(msg_limit);
                int device_typeIndex = cursor.getColumnIndexOrThrow(device_type);
                int roomIndex = cursor.getColumnIndexOrThrow(room);
                int original_nameIndex = cursor.getColumnIndexOrThrow(original_name);
                int blockIndex = cursor.getColumnIndexOrThrow(block);
                int iamblockedIndex = cursor.getColumnIndexOrThrow(iamblocked);


                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String sent_time = cursor.getString(sent_timeIndex);
                String dataType = cursor.getString(dataTypeIndex);
                String messages = cursor.getString(messagesIndex);
                String f_token = cursor.getString(f_tokenIndex);
                String msg_limit = cursor.getString(msg_limitIndex);
                String deviceType = cursor.getString(device_typeIndex);
                String original_name = cursor.getString(original_nameIndex);
                int block = cursor.getInt(blockIndex);
                int iamblocked = cursor.getInt(iamblockedIndex);

                int notification = cursor.getInt(notificationIndex);
                String room = cursor.getString(roomIndex);

                boolean blockValue = false;
                if (block == 1) {
                    blockValue = true;
                } else {
                    blockValue = false;
                }
                boolean iamblockedValue = false;
                if (iamblocked == 1) {
                    iamblockedValue = true;
                } else {
                    iamblockedValue = false;
                }

                get_user_active_contact_list_Model dataModel = new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, notification, msg_limit, deviceType, room, original_name, blockValue, iamblockedValue);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_user_active_chat_list


    //TODO : - get_user_active_chat_listMessageLimit
    public ArrayList<get_user_active_contact_list_MessageLmt_Model> getAllDataMessageLimit() {
        ArrayList<get_user_active_contact_list_MessageLmt_Model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_user_active_chat_list, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int notificationIndex = cursor.getColumnIndexOrThrow(notification);
                int msg_limitIndex = cursor.getColumnIndexOrThrow(msg_limit);
                int blockIndex = cursor.getColumnIndexOrThrow(block);

                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String msg_limit = cursor.getString(msg_limitIndex);
                int block = cursor.getInt(blockIndex);

                int notification = cursor.getInt(notificationIndex);

                boolean blockValue = false;
                if (block == 1) {
                    blockValue = true;
                } else {
                    blockValue = false;
            }

                //String photo, String full_name, String mobile_no, String caption, String uid, String msgLmt

                get_user_active_contact_list_MessageLmt_Model dataModel = new get_user_active_contact_list_MessageLmt_Model(photo, full_name, mobile_no, caption, uid, msg_limit,blockValue);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_user_active_chat_listInsideGroup
    public ArrayList<get_user_active_contact_list_Model> getAllDataInsideGroup() {
        ArrayList<get_user_active_contact_list_Model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_user_active_chat_list, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int sent_timeIndex = cursor.getColumnIndexOrThrow(sent_time);
                int dataTypeIndex = cursor.getColumnIndexOrThrow(dataType);
                int messagesIndex = cursor.getColumnIndexOrThrow(message);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);
                int notificationIndex = cursor.getColumnIndexOrThrow(notification);
                int msg_limitIndex = cursor.getColumnIndexOrThrow(msg_limit);
                int device_typeIndex = cursor.getColumnIndexOrThrow(device_type);
                int roomIndex = cursor.getColumnIndexOrThrow(room);
                int blockIndex = cursor.getColumnIndexOrThrow(block);
                int iamblockedIndex = cursor.getColumnIndexOrThrow(iamblocked);

                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String sent_time = cursor.getString(sent_timeIndex);
                String dataType = cursor.getString(dataTypeIndex);
                String messages = cursor.getString(messagesIndex);
                String f_token = cursor.getString(f_tokenIndex);
                String msg_limit = cursor.getString(msg_limitIndex);
                String deviceType = cursor.getString(device_typeIndex);

                int notification = cursor.getInt(notificationIndex);
                int block = cursor.getInt(blockIndex);
                int iamblocked = cursor.getInt(iamblockedIndex);
                String room = cursor.getString(roomIndex);

                //String photo, String full_name, String mobile_no, String caption, String uid, String msgLmt

                boolean blockValue = false;
                if (block == 1) {
                    blockValue = true;
                } else {
                    blockValue = false;
                }

                boolean iamblockedValue = false;
                if (iamblocked == 1) {
                    iamblockedValue = true;
                } else {
                    iamblockedValue = false;

                }
                get_user_active_contact_list_Model dataModel = new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, notification, msg_limit, deviceType, room, "", blockValue, iamblockedValue);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - group_chatsTable
//    public ArrayList<group_messageModel> getAllDataGroupChatsTable(String receiverRoom) {
//        ArrayList<group_messageModel> dataList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + group_chatsTable + " WHERE group_chatsTableId=" + receiverRoom, null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                // Print cursor content for debugging
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    String columnName = cursor.getColumnName(i);
//                    String columnValue = cursor.getString(i);
//                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
//                }
//
//                int uidIndex = cursor.getColumnIndexOrThrow(uid);
//                int messageIndex = cursor.getColumnIndexOrThrow(message);
//                int timeIndex = cursor.getColumnIndexOrThrow(time);
//                int documentIndex = cursor.getColumnIndexOrThrow(document);
//                int dataTypeIndex = cursor.getColumnIndexOrThrow(dataType);
//                int extensionIndex = cursor.getColumnIndexOrThrow(extension);
//                int nameIndex = cursor.getColumnIndexOrThrow(name);
//                int phoneIndex = cursor.getColumnIndexOrThrow(phone);
//                int miceTimingIndex = cursor.getColumnIndexOrThrow(miceTiming);
//                int micPhotoIndex = cursor.getColumnIndexOrThrow(micPhoto);
//                int createdByIndex = cursor.getColumnIndexOrThrow(createdBy);
//                int userNameIndex = cursor.getColumnIndexOrThrow(userName);
//                int modelIdIndex = cursor.getColumnIndexOrThrow(modelId);
//                int receiverUidIndex = cursor.getColumnIndexOrThrow(receiverUid);
//                int docSizeIndex = cursor.getColumnIndexOrThrow(docSize);
//                int fileNameIndex = cursor.getColumnIndexOrThrow(fileName);
//                int thumbnailIndex = cursor.getColumnIndexOrThrow(thumbnail);
//                int fileNameThumbnailIndex = cursor.getColumnIndexOrThrow(fileNameThumbnail);
//                int captionIndex = cursor.getColumnIndexOrThrow(caption);
//
//                String uid = cursor.getString(uidIndex);
//                String message = cursor.getString(messageIndex);
//                String time = cursor.getString(timeIndex);
//                String document = cursor.getString(documentIndex);
//                String dataType = cursor.getString(dataTypeIndex);
//                String extension = cursor.getString(extensionIndex);
//                String name = cursor.getString(nameIndex);
//                String phone = cursor.getString(phoneIndex);
//                String miceTiming = cursor.getString(miceTimingIndex);
//                String micPhoto = cursor.getString(micPhotoIndex);
//                String createdBy = cursor.getString(createdByIndex);
//                String userName = cursor.getString(userNameIndex);
//                String modelId = cursor.getString(modelIdIndex);
//                String receiverUid = cursor.getString(receiverUidIndex);
//                String docSize = cursor.getString(docSizeIndex);
//                String fileName = cursor.getString(fileNameIndex);
//                String thumbnail = cursor.getString(thumbnailIndex);
//                String fileNameThumbnail = cursor.getString(fileNameThumbnailIndex);
//                String caption = cursor.getString(captionIndex);
//
//
//                group_messageModel dataModel = new group_messageModel(uid, message, time, document, dataType, extension, name, phone, miceTiming, micPhoto, createdBy, userName, modelId, receiverUid, docSize, fileName, thumbnail, fileNameThumbnail, caption, Constant.getCurrentDate());
//                dataList.add(dataModel);
//            }
//            cursor.close();
//        }
//
//        db.close();
//        return dataList;
//    }


    //TODO : - get_group_listTable
    public ArrayList<grp_list_child_model> getAllDataGrpListChildModel() {
        ArrayList<grp_list_child_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_group_listTable, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Columnget_group_listTable: " + columnName + ", Value: " + columnValue);
                }

                int group_idIndex = cursor.getColumnIndexOrThrow(group_id);
                int sr_nosIndex = cursor.getColumnIndexOrThrow(sr_nos);
                int group_nameIndex = cursor.getColumnIndexOrThrow(group_name);
                int group_iconIndex = cursor.getColumnIndexOrThrow(group_icon);
                int group_created_byIndex = cursor.getColumnIndexOrThrow(group_created_by);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);
                int group_members_countIndex = cursor.getColumnIndexOrThrow(group_members_count);
                int sentTimeIndex = cursor.getColumnIndexOrThrow(sentTime);
                int dec_flgIndex = cursor.getColumnIndexOrThrow(dec_flg);
                int l_msgIndex = cursor.getColumnIndexOrThrow(l_msg);
                int data_typeIndex = cursor.getColumnIndexOrThrow(data_type);

                String group_id = cursor.getString(group_idIndex);
                int sr_nos = cursor.getInt(sr_nosIndex);
                String group_name = cursor.getString(group_nameIndex);
                String group_icon = cursor.getString(group_iconIndex);
                String group_created_by = cursor.getString(group_created_byIndex);
                String f_tokens = cursor.getString(f_tokenIndex);
                String group_members_count = cursor.getString(group_members_countIndex);
                String sentTime = cursor.getString(sentTimeIndex);
                String dec_flg = cursor.getString(dec_flgIndex);
                String l_msg = cursor.getString(l_msgIndex);
                String data_type = cursor.getString(data_typeIndex);

                ArrayList<grp_list_child2_model> grp_list_child2_modelList = new ArrayList<>();
                SQLiteDatabase db2 = this.getReadableDatabase();
                Cursor cursor2 = db2.rawQuery("SELECT * FROM " + grp_list_child2_modelTable + " WHERE group_id =" + group_id, null);
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        for (int i = 0; i < cursor2.getColumnCount(); i++) {
                            String columnName = cursor2.getColumnName(i);
                            String columnValue = cursor2.getString(i);
                            System.out.println("Column_child: " + columnName + ", Value: " + columnValue);
                        }

                        int friend_idIndex = cursor2.getColumnIndexOrThrow(friend_id);
                        int f_token2Index = cursor2.getColumnIndexOrThrow(f_token);
                        int device_typeIndex = cursor2.getColumnIndexOrThrow(device_type);

                        String friend_id = cursor2.getString(friend_idIndex);
                        String f_token2 = cursor2.getString(f_token2Index);
                        String device_type = cursor2.getString(device_typeIndex);

                        grp_list_child2_modelList.add(new grp_list_child2_model(friend_id, f_token2, device_type));
                    }
                    cursor2.close();
                }


                grp_list_child_model dataModel = new grp_list_child_model(sr_nos, group_id, group_name, group_icon, group_created_by, f_tokens, group_members_count, grp_list_child2_modelList, sentTime, Integer.parseInt(dec_flg), l_msg, data_type);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_user_active_chat_list
    public profileDBModel getAllDataProfile(String uids) {
        profileDBModel model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_profileTable + " WHERE uid =" + uids, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);


                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String f_token = cursor.getString(f_tokenIndex);

                model = new profileDBModel(full_name, caption, mobile_no, photo, f_token);

            }
            cursor.close();
        }

        db.close();
        return model;
    }


    //TODO : - get_user_active_chat_list
    public linkPreviewModel getAllLinkPreviewModel(String modelIds) {
        linkPreviewModel model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + linkPreviewTable + " WHERE " + modelId + " = ?", new String[]{modelIds});


        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int modelIdIndex = cursor.getColumnIndexOrThrow(modelId);
                int urlIndex = cursor.getColumnIndexOrThrow(url);
                int titleIndex = cursor.getColumnIndexOrThrow(title);
                int descriptionIndex = cursor.getColumnIndexOrThrow(description);
                int favIconIndex = cursor.getColumnIndexOrThrow(favIcon);
                int image_urlIndex = cursor.getColumnIndexOrThrow(image_url);


                String modelIdstr = cursor.getString(modelIdIndex);
                String url = cursor.getString(urlIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);
                String favIcon = cursor.getString(favIconIndex);
                String image_url = cursor.getString(image_urlIndex);

                model = new linkPreviewModel(modelIdstr, url, title, description, favIcon, image_url);

            }
            cursor.close();
        }

        db.close();
        return model;
    }


    //TODO : - get_user_active_chat_listCheckData
    public linkPreviewModel getAllLinkPreviewModelCheckData(String modelIds) {
        linkPreviewModel model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + linkPreviewTable + " WHERE modelId = ?", new String[]{modelIds});


        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int modelIdIndex = cursor.getColumnIndexOrThrow(modelId);
                int urlIndex = cursor.getColumnIndexOrThrow(url);
                int titleIndex = cursor.getColumnIndexOrThrow(title);
                int descriptionIndex = cursor.getColumnIndexOrThrow(description);
                int favIconIndex = cursor.getColumnIndexOrThrow(favIcon);
                int image_urlIndex = cursor.getColumnIndexOrThrow(image_url);


                String modelIdstr = cursor.getString(modelIdIndex);
                String url = cursor.getString(urlIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);
                String favIcon = cursor.getString(favIconIndex);
                String image_url = cursor.getString(image_urlIndex);

                model = new linkPreviewModel(modelIdstr, url, title, description, favIcon, image_url);

            }
            cursor.close();
        }

        db.close();
        return model;
    }

    //TODO : - getAllDataget_get_user_profile_imagesTablel
    public ArrayList<profilestatusModel> getAllDataget_get_user_profile_imagesTablel(String uids) {
        ArrayList<profilestatusModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modify the SQL query to retrieve last 4 entries
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_user_profile_imagesTable + " WHERE uid =" + uids + " ORDER BY " + id + " DESC LIMIT 4", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndexOrThrow(id);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);

                String id = cursor.getString(idIndex);
                String photo = cursor.getString(photoIndex);
                String f_token = cursor.getString(f_tokenIndex);

                profilestatusModel profilestatusModel = new profilestatusModel(photo, id);
                dataList.add(profilestatusModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - getAllDataget_get_user_profile_imagesTablel
    public ArrayList<get_calling_contact_list_model> getAllDataget_get_user_profile_imagesTablel() {
        ArrayList<get_calling_contact_list_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Modify the SQL query to retrieve last 4 entries
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_user_active_chat_list, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);

                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String f_token = cursor.getString(f_tokenIndex);

                get_calling_contact_list_model get_calling_contact_list_model = new get_calling_contact_list_model(uid, photo, full_name, mobile_no, caption, f_token);
                dataList.add(get_calling_contact_list_model);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_voice_call_logTable
    public ArrayList<callloglistModel> get_voice_call_logTable() {
        ArrayList<callloglistModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_voice_call_logTable, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int dateIndex = cursor.getColumnIndexOrThrow(date);

                String dates = cursor.getString(dateIndex);
                Log.d("Column", "get_voice_call_logTable: " + dates);

                ArrayList<callingUserInfoChildModel> callingUserInfoChildModelList = new ArrayList<>();
                SQLiteDatabase db2 = this.getReadableDatabase();
                Cursor cursor2 = db2.rawQuery("SELECT * FROM " + get_voice_call_logChild1Table + " WHERE date = '" + dates + "'", null);

                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        for (int i = 0; i < cursor2.getColumnCount(); i++) {
                            String columnName = cursor2.getColumnName(i);
                            String columnValue = cursor2.getString(i);
                            System.out.println("Column_child: " + columnName + ", Value: " + columnValue);
                        }

                        int friend_idIndex = cursor2.getColumnIndexOrThrow(friend_id);
                        int photoIndex = cursor2.getColumnIndexOrThrow(photo);
                        int full_nameIndex = cursor2.getColumnIndexOrThrow(full_name);
                        int f_tokenIndex = cursor2.getColumnIndexOrThrow(f_token);
                        int end_timeIndex = cursor2.getColumnIndexOrThrow(end_time);
                        int calling_flagIndex = cursor2.getColumnIndexOrThrow(calling_flag);
                        int call_typeIndex = cursor2.getColumnIndexOrThrow(call_type);
                        int mobile_noIndex = cursor2.getColumnIndexOrThrow(mobile_no);
                        int start_timeIndex = cursor2.getColumnIndexOrThrow(start_time);
                        int date2Index = cursor2.getColumnIndexOrThrow(date);
                        int device_typeIndex = cursor2.getColumnIndexOrThrow(device_type);


                        String friend_id = cursor2.getString(friend_idIndex);
                        String photo = cursor2.getString(photoIndex);
                        String full_name = cursor2.getString(full_nameIndex);
                        String f_token = cursor2.getString(f_tokenIndex);
                        String end_time = cursor2.getString(end_timeIndex);
                        String calling_flag = cursor2.getString(calling_flagIndex);
                        String call_type = cursor2.getString(call_typeIndex);
                        String mobile_no = cursor2.getString(mobile_noIndex);
                        String start_time = cursor2.getString(start_timeIndex);
                        String dates2 = cursor2.getString(date2Index);
                        String device_type = cursor2.getString(device_typeIndex);


                        callingUserInfoChildModelList.add(new callingUserInfoChildModel(friend_id, photo, full_name, f_token, end_time, calling_flag, call_type, mobile_no, start_time, dates2, device_type));
                    }
                    cursor2.close();
                }


                callloglistModel dataModel = new callloglistModel(dates, callingUserInfoChildModelList);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - deleteVoiceCallLog
    public void deleteVoiceCallLog() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete data from the child table first
        db.delete(get_voice_call_logChild1Table, null, null);
        db.delete(get_voice_call_logChild2Table, null, null);

        // Then delete data from the main table
        db.delete(get_voice_call_logTable, null, null);

        db.close();
    }

    //TODO : - deleteVoiceCallLog
    public void deleteVideoCallLog() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete data from the child table first
        db.delete(get_voice_call_logChild1VIDEOTable, null, null);
        db.delete(get_voice_call_logChild2VIDEOTable, null, null);

        // Then delete data from the main table
        db.delete(get_voice_call_logVIDEOTable, null, null);

        db.close();
    }


    //TODO : - get_voice_call_logTable
    public ArrayList<call_log_history_model> get_voice_call_logChild2Table(String friendId, String dateobj) {
        ArrayList<call_log_history_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_voice_call_logChild2Table + " WHERE friend_id = " + friendId + " AND date ='" + dateobj + "'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int idIndex = cursor.getColumnIndexOrThrow(id);
                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int friend_idIndex = cursor.getColumnIndexOrThrow(friend_id);
                int dateIndex = cursor.getColumnIndexOrThrow(date);
                int start_timeIndex = cursor.getColumnIndexOrThrow(start_time);
                int end_timeIndex = cursor.getColumnIndexOrThrow(end_time);
                int calling_flagIndex = cursor.getColumnIndexOrThrow(calling_flag);
                int call_typeIndex = cursor.getColumnIndexOrThrow(call_type);

                String id = cursor.getString(idIndex);
                String uid = cursor.getString(uidIndex);
                String friend_id = cursor.getString(friend_idIndex);
                String date = cursor.getString(dateIndex);
                String start_time = cursor.getString(start_timeIndex);
                String end_time = cursor.getString(end_timeIndex);
                String calling_flag = cursor.getString(calling_flagIndex);
                String call_type = cursor.getString(call_typeIndex);


                call_log_history_model dataModel = new call_log_history_model(id, uid, friend_id, date, start_time, end_time, calling_flag, call_type);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_voice_call_logVIDEOTable
    public ArrayList<callloglistModel> get_voice_call_logVIDEOTable() {
        ArrayList<callloglistModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_voice_call_logVIDEOTable, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int dateIndex = cursor.getColumnIndexOrThrow(date);

                String dates = cursor.getString(dateIndex);
                Log.d("Column", "get_voice_call_logTable: " + dates);

                ArrayList<callingUserInfoChildModel> callingUserInfoChildModelList = new ArrayList<>();
                SQLiteDatabase db2 = this.getReadableDatabase();
                Cursor cursor2 = db2.rawQuery("SELECT * FROM " + get_voice_call_logChild1VIDEOTable + " WHERE date = '" + dates + "'", null);

                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        for (int i = 0; i < cursor2.getColumnCount(); i++) {
                            String columnName = cursor2.getColumnName(i);
                            String columnValue = cursor2.getString(i);
                            System.out.println("Column_child: " + columnName + ", Value: " + columnValue);
                        }

                        int friend_idIndex = cursor2.getColumnIndexOrThrow(friend_id);
                        int photoIndex = cursor2.getColumnIndexOrThrow(photo);
                        int full_nameIndex = cursor2.getColumnIndexOrThrow(full_name);
                        int f_tokenIndex = cursor2.getColumnIndexOrThrow(f_token);
                        int end_timeIndex = cursor2.getColumnIndexOrThrow(end_time);
                        int calling_flagIndex = cursor2.getColumnIndexOrThrow(calling_flag);
                        int call_typeIndex = cursor2.getColumnIndexOrThrow(call_type);
                        int mobile_noIndex = cursor2.getColumnIndexOrThrow(mobile_no);
                        int start_timeIndex = cursor2.getColumnIndexOrThrow(start_time);
                        int date2Index = cursor2.getColumnIndexOrThrow(date);
                        int device_typeIndex = cursor2.getColumnIndexOrThrow(device_type);


                        String friend_id = cursor2.getString(friend_idIndex);
                        String photo = cursor2.getString(photoIndex);
                        String full_name = cursor2.getString(full_nameIndex);
                        String f_token = cursor2.getString(f_tokenIndex);
                        String end_time = cursor2.getString(end_timeIndex);
                        String calling_flag = cursor2.getString(calling_flagIndex);
                        String call_type = cursor2.getString(call_typeIndex);
                        String mobile_no = cursor2.getString(mobile_noIndex);
                        String start_time = cursor2.getString(start_timeIndex);
                        String dates2 = cursor2.getString(date2Index);
                        String device_type = cursor2.getString(device_typeIndex);


                        callingUserInfoChildModelList.add(new callingUserInfoChildModel(friend_id, photo, full_name, f_token, end_time, calling_flag, call_type, mobile_no, start_time, dates2, device_type));
                    }
                    cursor2.close();
                }


                callloglistModel dataModel = new callloglistModel(dates, callingUserInfoChildModelList);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_voice_call_logTable
    public ArrayList<call_log_history_model> get_voice_call_logChild2VIDEOTable(String friendId, String dateobj) {
        ArrayList<call_log_history_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_voice_call_logChild2VIDEOTable + " WHERE friend_id = " + friendId + " AND date ='" + dateobj + "'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int idIndex = cursor.getColumnIndexOrThrow(id);
                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int friend_idIndex = cursor.getColumnIndexOrThrow(friend_id);
                int dateIndex = cursor.getColumnIndexOrThrow(date);
                int start_timeIndex = cursor.getColumnIndexOrThrow(start_time);
                int end_timeIndex = cursor.getColumnIndexOrThrow(end_time);
                int calling_flagIndex = cursor.getColumnIndexOrThrow(calling_flag);
                int call_typeIndex = cursor.getColumnIndexOrThrow(call_type);

                String id = cursor.getString(idIndex);
                String uid = cursor.getString(uidIndex);
                String friend_id = cursor.getString(friend_idIndex);
                String date = cursor.getString(dateIndex);
                String start_time = cursor.getString(start_timeIndex);
                String end_time = cursor.getString(end_timeIndex);
                String calling_flag = cursor.getString(calling_flagIndex);
                String call_type = cursor.getString(call_typeIndex);


                call_log_history_model dataModel = new call_log_history_model(id, uid, friend_id, date, start_time, end_time, calling_flag, call_type);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_users_all_contactTableTable
    public ArrayList<get_contact_model> get_users_all_contactTable() {
        ArrayList<get_contact_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_users_all_contactTable + " LIMIT 50", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);
                int device_typeIndex = cursor.getColumnIndexOrThrow(device_type);
                int blockIndex = cursor.getColumnIndexOrThrow(block);

                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String f_token = cursor.getString(f_tokenIndex);
                String device_type = cursor.getString(device_typeIndex);
                int block = cursor.getInt(blockIndex);
                // Log.d("Column", "get_voice_call_logTable: " + dates);

                boolean blockValue = false;
                if (block == 1) {
                    blockValue = true;
                } else {
                    blockValue = false;
                }


                get_contact_model dataModel = new get_contact_model(uid, photo, full_name, mobile_no, caption, f_token, device_type,blockValue);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    //TODO : - get_users_all_contactTableTable
    public ArrayList<allContactListModel> get_users_all_contactTableInviteSpecial() {
        ArrayList<allContactListModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_users_all_contactTableInviteSpecial + " LIMIT 50", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int uidIndex = cursor.getColumnIndexOrThrow(uid);
                int photoIndex = cursor.getColumnIndexOrThrow(photo);
                int full_nameIndex = cursor.getColumnIndexOrThrow(full_name);
                int mobile_noIndex = cursor.getColumnIndexOrThrow(mobile_no);
                int captionIndex = cursor.getColumnIndexOrThrow(caption);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);
                int device_typeIndex = cursor.getColumnIndexOrThrow(device_type);
                int c_flagIndex = cursor.getColumnIndexOrThrow(c_flag);
                int contact_nameIndex = cursor.getColumnIndexOrThrow(contact_name);
                int contact_numberIndex = cursor.getColumnIndexOrThrow(contact_number);
                int block_index = cursor.getColumnIndexOrThrow(block);
                int iamblocked_index = cursor.getColumnIndexOrThrow(iamblocked);

                String uid = cursor.getString(uidIndex);
                String photo = cursor.getString(photoIndex);
                String full_name = cursor.getString(full_nameIndex);
                String mobile_no = cursor.getString(mobile_noIndex);
                String caption = cursor.getString(captionIndex);
                String f_token = cursor.getString(f_tokenIndex);
                String device_type = cursor.getString(device_typeIndex);
                String c_flag = cursor.getString(c_flagIndex);
                String contact_names = cursor.getString(contact_nameIndex);
                String contact_numbers = cursor.getString(contact_numberIndex);
                int block = cursor.getInt(block_index);
                int iamblocked = cursor.getInt(iamblocked_index);

                boolean blockValue = false;
                if (block == 1) {
                    blockValue = true;
                } else {
                    blockValue = false;
                }

                boolean iamblockedValue = false;
                if (iamblocked == 1) {
                    iamblockedValue = true;
                } else {
                    iamblockedValue = false;
                }

                allContactListModel dataModel = new allContactListModel(c_flag, uid, photo, full_name, mobile_no, caption, f_token, device_type, contact_names, contact_numbers, blockValue,iamblockedValue);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    // Constant.get_contact_invite_model_List.add(new get_contact_invite_model(contact_name, contact_number, f_token));

    //TODO : - get_users_all_contactCHILDTable
    public ArrayList<get_contact_invite_model> get_users_all_contactCHILDTable() {
        ArrayList<get_contact_invite_model> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + get_users_all_contactCHILDTable + " LIMIT 50", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }


                int contact_nameIndex = cursor.getColumnIndexOrThrow(contact_name);
                int contact_numberIndex = cursor.getColumnIndexOrThrow(contact_number);
                int f_tokenIndex = cursor.getColumnIndexOrThrow(f_token);

                String contact_name = cursor.getString(contact_nameIndex);
                String contact_number = cursor.getString(contact_numberIndex);
                String f_token = cursor.getString(f_tokenIndex);


                get_contact_invite_model dataModel = new get_contact_invite_model(contact_name, contact_number, f_token);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }


    // todo delete individual chatting

    public void deleteIndividualChatting(String modelIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(chatsTable, modelId + " = ?", new String[]{modelIds});
        db.close();
    }


    // todo delete individual chatting only for text data  SENDERSIDE ONLY
    public void updateIndividualChatting(String modelIds, String newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(message, newValue); // Assuming columnName is the name of the column you want to update

        db.update(chatsTable, values, modelId + " = ?", new String[]{modelIds});
        db.close();
    }


    // todo delete group chatting

    public void deleteIndividualgroup_chatsTable(String modelIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(group_chatsTable, modelId + " = ?", new String[]{modelIds});
        db.close();
    }


    // todo delete group chatting only for text data  SENDERSIDE ONLY
    public void updateIndividualgroup_chatsTable(String modelIds, String newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(message, newValue); // Assuming columnName is the name of the column you want to update

        db.update(group_chatsTable, values, modelId + " = ?", new String[]{modelIds});
        db.close();
    }


    // todo delete group chatting

    public void deleteIndividuaChatting(String uids) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(get_user_active_chat_list, uid + " = ?", new String[]{uids});
        db.close();
    }


    public void deleteAllChatting() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(get_user_active_chat_list, null, null);
        db.close();
    }


    //TODO : - get_user_active_chat_list
    public get_user_active_contact_list_Model getSingleDataNotification(String receiverUid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE uid = ?", get_user_active_chat_list);
        Cursor cursor = db.rawQuery(query, new String[]{receiverUid});

        if (cursor != null && cursor.moveToFirst()) {
            // Print cursor content for debugging
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);
                System.out.println("Column: " + columnName + ", Value: " + columnValue);
            }

            int uidIndex = cursor.getColumnIndexOrThrow("uid");
            int photoIndex = cursor.getColumnIndexOrThrow("photo");
            int full_nameIndex = cursor.getColumnIndexOrThrow("full_name");
            int mobile_noIndex = cursor.getColumnIndexOrThrow("mobile_no");
            int captionIndex = cursor.getColumnIndexOrThrow("caption");
            int sent_timeIndex = cursor.getColumnIndexOrThrow("sent_time");
            int dataTypeIndex = cursor.getColumnIndexOrThrow("dataType");
            int messagesIndex = cursor.getColumnIndexOrThrow("message");
            int f_tokenIndex = cursor.getColumnIndexOrThrow("f_token");
            int notificationIndex = cursor.getColumnIndexOrThrow("notification");
            int msg_limitIndex = cursor.getColumnIndexOrThrow("msg_limit");
            int device_typeIndex = cursor.getColumnIndexOrThrow("device_type");
            int roomIndex = cursor.getColumnIndexOrThrow("room");
            int original_nameIndex = cursor.getColumnIndexOrThrow("room");
            int blockIndex = cursor.getColumnIndexOrThrow("block");
            int iamblockedIndex = cursor.getColumnIndexOrThrow("iamblocked");


            String uid = cursor.getString(uidIndex);
            String photo = cursor.getString(photoIndex);
            String full_name = cursor.getString(full_nameIndex);
            String mobile_no = cursor.getString(mobile_noIndex);
            String caption = cursor.getString(captionIndex);
            String sent_time = cursor.getString(sent_timeIndex);
            String dataType = cursor.getString(dataTypeIndex);
            String messages = cursor.getString(messagesIndex);
            String f_token = cursor.getString(f_tokenIndex);
            String msg_limit = cursor.getString(msg_limitIndex);
            String deviceType = cursor.getString(device_typeIndex);
            int notification = cursor.getInt(notificationIndex);
            int block = cursor.getInt(blockIndex);
            int iamblocked = cursor.getInt(iamblockedIndex);


            String room = cursor.getString(roomIndex);
            String original_name = cursor.getString(original_nameIndex);

            cursor.close();
            db.close();

            boolean blockValue = false;
            if (block == 1) {
                blockValue = true;
            } else {
                blockValue = false;
            }

            boolean iamblockedValue = false;
            if (iamblocked == 1) {
                iamblockedValue = true;
            } else {
                iamblockedValue = false;
            }

            return new get_user_active_contact_list_Model(photo, full_name, mobile_no, caption, uid, sent_time, dataType, messages, f_token, notification, msg_limit, deviceType, room, original_name, blockValue, iamblockedValue);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null; // Return null if no data is found
    }

    public boolean isModelIdExists(String modelId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("chats", new String[]{"modelId"},
                "modelId = ?", new String[]{modelId}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public ArrayList<Emoji> getEmojiIcon() {
        ArrayList<Emoji> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + fetch_emoji_dataTable, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Print cursor content for debugging
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    System.out.println("Column: " + columnName + ", Value: " + columnValue);
                }

                int slugIndex = cursor.getColumnIndexOrThrow(slug);
                int characterIndex = cursor.getColumnIndexOrThrow(character);
                int unicodeNameIndex = cursor.getColumnIndexOrThrow(unicodeName);
                int codePointIndex = cursor.getColumnIndexOrThrow(codePoint);
                int groupsIndex = cursor.getColumnIndexOrThrow(groups);
                int subGroupIndex = cursor.getColumnIndexOrThrow(subGroup);

                String slug = cursor.getString(slugIndex);
                String character = cursor.getString(characterIndex);
                String unicodeName = cursor.getString(unicodeNameIndex);
                String codePoint = cursor.getString(codePointIndex);
                String groups = cursor.getString(groupsIndex);
                String subGroup = cursor.getString(subGroupIndex);


                //String photo, String full_name, String mobile_no, String caption, String uid, String msgLmt

                Emoji dataModel = new Emoji(slug, character, unicodeName, codePoint, groups, subGroup);
                dataList.add(dataModel);
            }
            cursor.close();
        }

        db.close();
        return dataList;
    }

    public void insertMessage(messagemodel2 message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", message.getUid());
        values.put("message", message.getMessage());
        values.put("time", message.getTime());
        values.put("document", message.getDocument());
        values.put("dataType", message.getDataType());
        values.put("extension", message.getExtension());
        values.put("name", message.getName());
        values.put("phone", message.getPhone());
        values.put("micPhoto", message.getMicPhoto());
        values.put("miceTiming", message.getMiceTiming());
        values.put("userName", message.getUserName());
        values.put("replytextData", message.getReplytextData());
        values.put("replyKey", message.getReplyKey());
        values.put("replyType", message.getReplyType());
        values.put("replyOldData", message.getReplyOldData());
        values.put("replyCrtPostion", message.getReplyCrtPostion());
        values.put("modelId", message.getModelId());
        values.put("receiverUid", message.getReceiverUid());
        values.put("forwaredKey", message.getForwaredKey());
        values.put("groupName", message.getGroupName());
        values.put("docSize", message.getDocSize());
        values.put("fileName", message.getFileName());
        values.put("thumbnail", message.getThumbnail());
        values.put("fileNameThumbnail", message.getFileNameThumbnail());
        values.put("caption", message.getCaption());
        values.put("notification", message.getNotification());
        values.put("currentDate", message.getCurrentDate());
        values.put("imageWidthDp", message.getImageWidth());
        values.put("imageHeightDp", message.getImageHeight());
        values.put("aspectRatio", message.getAspectRatio());

        // Serialize emojiModel list to JSON
        Gson gson = new Gson();
        String emojiJson = gson.toJson(message.getEmojiModel());
        values.put("emojiModel", emojiJson);

        values.put("emojiCount", message.getEmojiCount());
        values.put("timestamp", message.getTimestamp());
        values.put("active", message.getActive());

        // ✅ Replace if modelId + receiverUid already exists
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public boolean deleteMessageByModelId(String modelId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;

        try {
            // Define the DELETE query
            String query = "DELETE FROM " + TABLE_NAME + " WHERE modelId = ?";
            // Execute the query with the modelId parameter
            db.execSQL(query, new String[]{modelId});
            isDeleted = true;
            Log.d("deleteMessageByModelId", "Successfully deleted message with modelId: " + modelId);
        } catch (Exception e) {
            Log.e("deleteMessageByModelId", "Error deleting message: " + e.getMessage(), e);
        } finally {
            db.close();
        }

        return isDeleted;
    }


    private ArrayList<emojiModel> deserializeEmojiModel(String emojiJson) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<emojiModel>>() {
            }.getType();
            ArrayList<emojiModel> emojiList = gson.fromJson(emojiJson, listType);
            return emojiList != null ? emojiList : new ArrayList<>();
        } catch (Exception e) {
            Log.e("deserializeEmojiModel", "Error deserializing emojiModel: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }


    // ✅ Read All Messages
    public List<messagemodel2> getMessagesFromDatabase(String receiverUid) {
        List<messagemodel2> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE receiverUid = ? AND active = ?";
        Cursor cursor = db.rawQuery(query, new String[]{receiverUid, "0"});

        try {
            if (cursor.moveToFirst()) {
                do {
                    messagemodel2 model = new messagemodel2();

                    model.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
                    model.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
                    model.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                    model.setDocument(cursor.getString(cursor.getColumnIndexOrThrow("document")));
                    model.setDataType(cursor.getString(cursor.getColumnIndexOrThrow("dataType")));
                    model.setExtension(cursor.getString(cursor.getColumnIndexOrThrow("extension")));
                    model.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    model.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                    model.setMicPhoto(cursor.getString(cursor.getColumnIndexOrThrow("micPhoto")));
                    model.setMiceTiming(cursor.getString(cursor.getColumnIndexOrThrow("miceTiming")));
                    model.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
                    model.setReplytextData(cursor.getString(cursor.getColumnIndexOrThrow("replytextData")));
                    model.setReplyKey(cursor.getString(cursor.getColumnIndexOrThrow("replyKey")));
                    model.setReplyType(cursor.getString(cursor.getColumnIndexOrThrow("replyType")));
                    model.setReplyOldData(cursor.getString(cursor.getColumnIndexOrThrow("replyOldData")));
                    model.setReplyCrtPostion(cursor.getString(cursor.getColumnIndexOrThrow("replyCrtPostion")));
                    model.setModelId(cursor.getString(cursor.getColumnIndexOrThrow("modelId")));
                    model.setReceiverUid(cursor.getString(cursor.getColumnIndexOrThrow("receiverUid")));
                    model.setForwaredKey(cursor.getString(cursor.getColumnIndexOrThrow("forwaredKey")));
                    model.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow("groupName")));
                    model.setDocSize(cursor.getString(cursor.getColumnIndexOrThrow("docSize")));
                    model.setFileName(cursor.getString(cursor.getColumnIndexOrThrow("fileName")));
                    model.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail")));
                    model.setFileNameThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("fileNameThumbnail")));
                    model.setCaption(cursor.getString(cursor.getColumnIndexOrThrow("caption")));
                    model.setNotification(cursor.getInt(cursor.getColumnIndexOrThrow("notification")));
                    model.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow("currentDate")));
                    model.setEmojiCount(cursor.getString(cursor.getColumnIndexOrThrow("emojiCount")));
                    model.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
                    model.setImageWidth(cursor.getString(cursor.getColumnIndexOrThrow("imageWidthDp")));
                    model.setImageHeight(cursor.getString(cursor.getColumnIndexOrThrow("imageHeightDp")));
                    model.setAspectRatio(cursor.getString(cursor.getColumnIndexOrThrow("aspectRatio")));
                    model.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("active")));
                    model.setEmojiModel(deserializeEmojiModel(cursor.getString(cursor.getColumnIndexOrThrow("emojiModel"))));


                    list.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getMessagesFromDatabase", "Error fetching messages: " + e.getMessage(), e);
        } finally {
            cursor.close();
            db.close();
        }

        Log.d("getMessagesFromDatabase", "Successfully fetched " + list.size() + " messages for UID: " + receiverUid);
        return list;
    }

    public void insertMessageGroup(group_messageModel2 message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", message.getUid());
        values.put("message", message.getMessage());
        values.put("time", message.getTime());
        values.put("document", message.getDocument());
        values.put("dataType", message.getDataType());
        values.put("extension", message.getExtension());
        values.put("name", message.getName());
        values.put("phone", message.getPhone());
        values.put("micPhoto", message.getMicPhoto());
        values.put("miceTiming", message.getMiceTiming());
        values.put("createdBy", message.getCreatedBy());
        values.put("userName", message.getUserName());
        values.put("modelId", message.getModelId());
        values.put("receiverUid", message.getReceiverUid());
        values.put("docSize", message.getDocSize());
        values.put("fileName", message.getFileName());
        values.put("thumbnail", message.getThumbnail());
        values.put("fileNameThumbnail", message.getFileNameThumbnail());
        values.put("caption", message.getCaption());
        values.put("currentDate", message.getCurrentDate());
        values.put("senderRoom", message.getSenderRoom());
        values.put("active", message.getActive());

        // ✅ Replace if modelId + receiverUid already exists
        db.insertWithOnConflict(TABLE_NAME_GROUP, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<group_messageModel2> getMessagesFromDatabaseGroup(String senderRoom) {
        List<group_messageModel2> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_GROUP + " WHERE senderRoom = ? AND active = ?";
        Cursor cursor = db.rawQuery(query, new String[]{senderRoom, "0"});

        try {
            if (cursor.moveToFirst()) {
                do {
                    group_messageModel2 model = new group_messageModel2();

                    model.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
                    model.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
                    model.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                    model.setDocument(cursor.getString(cursor.getColumnIndexOrThrow("document")));
                    model.setDataType(cursor.getString(cursor.getColumnIndexOrThrow("dataType")));
                    model.setExtension(cursor.getString(cursor.getColumnIndexOrThrow("extension")));
                    model.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    model.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                    model.setMicPhoto(cursor.getString(cursor.getColumnIndexOrThrow("micPhoto")));
                    model.setMiceTiming(cursor.getString(cursor.getColumnIndexOrThrow("miceTiming")));
                    model.setCreatedBy(cursor.getString(cursor.getColumnIndexOrThrow("createdBy")));
                    model.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
                    model.setModelId(cursor.getString(cursor.getColumnIndexOrThrow("modelId")));
                    model.setReceiverUid(cursor.getString(cursor.getColumnIndexOrThrow("receiverUid")));
                    model.setDocSize(cursor.getString(cursor.getColumnIndexOrThrow("docSize")));
                    model.setFileName(cursor.getString(cursor.getColumnIndexOrThrow("fileName")));
                    model.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail")));
                    model.setFileNameThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("fileNameThumbnail")));
                    model.setCaption(cursor.getString(cursor.getColumnIndexOrThrow("caption")));
                    model.setImageWidth(cursor.getString(cursor.getColumnIndexOrThrow("imageWidthDp")));
                    model.setImageHeight(cursor.getString(cursor.getColumnIndexOrThrow("imageHeightDp")));
                    model.setAspectRatio(cursor.getString(cursor.getColumnIndexOrThrow("aspectRatio")));
                    model.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow("currentDate")));
                    model.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow("senderRoom")));
                    model.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("active")));

                    list.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getMessagesFromDatabase", "Error fetching messages: " + e.getMessage(), e);
        } finally {
            cursor.close();
            db.close();
        }

        Log.d("getMessagesFromDatabase", "Successfully fetched " + list.size() + " messages for UID: " + receiverUid);
        return list;
    }

    public boolean deleteMessageByModelIdGROUP(String modelId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;

        try {
            // Define the DELETE query
            String query = "DELETE FROM " + TABLE_NAME_GROUP + " WHERE modelId = ?";
            // Execute the query with the modelId parameter
            db.execSQL(query, new String[]{modelId});
            isDeleted = true;
            Log.d("deleteMessageByModelId", "Successfully deleted message with modelId: " + modelId);
        } catch (Exception e) {
            Log.e("deleteMessageByModelId", "Error deleting message: " + e.getMessage(), e);
        } finally {
            db.close();
        }

        return isDeleted;
    }

    public get_contact_model getContactByUid(String uid) {
        get_contact_model contact = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT full_name, photo, mobile_no, f_token, device_type FROM " + get_users_all_contactTable + " WHERE uid = ?";
            cursor = db.rawQuery(query, new String[]{uid});
            if (cursor.moveToFirst()) {
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"));
                String mobileNo = cursor.getString(cursor.getColumnIndexOrThrow("mobile_no"));
                String fToken = cursor.getString(cursor.getColumnIndexOrThrow("f_token"));
                String deviceType = cursor.getString(cursor.getColumnIndexOrThrow("device_type"));
                contact = new get_contact_model();
                contact.setUid(uid);
                contact.setFull_name(fullName != null ? fullName : "Unknown");
                contact.setPhoto(photo != null && isValidUrl(photo) ? photo : "file:///android_asset/user.png");
                contact.setMobile_no(mobileNo);
                contact.setF_token(fToken);
                contact.setDevice_type(deviceType);

                Log.d(TAG, "getContactByUid - UID: " + uid + ", Full Name: " + fullName + ", Photo: " + photo);
            } else {
                Log.d(TAG, "No contact found for UID: " + uid);
                contact = new get_contact_model();
                contact.setUid(uid);
                contact.setFull_name("Unknown");
                contact.setPhoto("file:///android_asset/user.png");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching contact by UID: " + e.getMessage());
            contact = new get_contact_model();
            contact.setUid(uid);
            contact.setFull_name("Unknown");
            contact.setPhoto("file:///android_asset/user.png");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contact;
    }


    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) return false;
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Invalid URL: " + url + ", Error: " + e.getMessage());
            return false;
        }
    }

    // ==================== PENDING MESSAGES METHODS ====================

    /**
     * Insert a message into pending messages table (before upload)
     */
    public void insertPendingMessage(messageModel message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("uid", message.getUid());
        values.put("message", message.getMessage());
        values.put("time", message.getTime());
        values.put("document", message.getDocument());
        values.put("dataType", message.getDataType());
        values.put("extension", message.getExtension());
        values.put("name", message.getName());
        values.put("phone", message.getPhone());
        values.put("micPhoto", message.getMicPhoto());
        values.put("miceTiming", message.getMiceTiming());
        values.put("userName", message.getUserName());
        values.put("replytextData", message.getReplytextData());
        values.put("replyKey", message.getReplyKey());
        values.put("replyType", message.getReplyType());
        values.put("replyOldData", message.getReplyOldData());
        values.put("replyCrtPostion", message.getReplyCrtPostion());
        values.put("modelId", message.getModelId());
        values.put("receiverUid", message.getReceiverUid());
        values.put("forwaredKey", message.getForwaredKey());
        values.put("groupName", message.getGroupName());
        values.put("docSize", message.getDocSize());
        values.put("fileName", message.getFileName());
        values.put("thumbnail", message.getThumbnail());
        values.put("fileNameThumbnail", message.getFileNameThumbnail());
        values.put("caption", message.getCaption());
        values.put("notification", message.getNotification());
        values.put("currentDate", message.getCurrentDate());
        values.put("emojiCount", message.getEmojiCount());
        values.put("timestamp", message.getTimestamp());
        values.put("imageWidthDp", message.getImageWidth());
        values.put("imageHeightDp", message.getImageHeight());
        values.put("aspectRatio", message.getAspectRatio());
        values.put("selectionCount", message.getSelectionCount());

        // Serialize emojiModel list to JSON
        Gson gson = new Gson();
        String emojiJson = gson.toJson(message.getEmojiModel());
        values.put("emojiModel", emojiJson);

        // Serialize selectionBunch list to JSON
        String selectionBunchJson = gson.toJson(message.getSelectionBunch());
        values.put("selectionBunch", selectionBunchJson);

        values.put("uploadStatus", 0); // 0 = pending, 1 = uploading, 2 = completed

        // Replace if modelId + receiverUid already exists
        db.insertWithOnConflict(TABLE_NAME_PENDING, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        
        Log.d("PendingMessages", "Message stored in pending table: " + message.getModelId());
    }

    /**
     * Get all pending messages for a receiver
     */
    public List<messageModel> getPendingMessages(String receiverUid) {
        List<messageModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_PENDING + " WHERE receiverUid = ? AND uploadStatus IN (0,1) ORDER BY timestamp ASC";
        Cursor cursor = db.rawQuery(query, new String[]{receiverUid});

        try {
            if (cursor.moveToFirst()) {
                do {
                    messageModel model = new messageModel();
                    
                    model.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
                    model.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
                    model.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                    model.setDocument(cursor.getString(cursor.getColumnIndexOrThrow("document")));
                    model.setDataType(cursor.getString(cursor.getColumnIndexOrThrow("dataType")));
                    model.setExtension(cursor.getString(cursor.getColumnIndexOrThrow("extension")));
                    model.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    model.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                    model.setMicPhoto(cursor.getString(cursor.getColumnIndexOrThrow("micPhoto")));
                    model.setMiceTiming(cursor.getString(cursor.getColumnIndexOrThrow("miceTiming")));
                    model.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
                    model.setReplytextData(cursor.getString(cursor.getColumnIndexOrThrow("replytextData")));
                    model.setReplyKey(cursor.getString(cursor.getColumnIndexOrThrow("replyKey")));
                    model.setReplyType(cursor.getString(cursor.getColumnIndexOrThrow("replyType")));
                    model.setReplyOldData(cursor.getString(cursor.getColumnIndexOrThrow("replyOldData")));
                    model.setReplyCrtPostion(cursor.getString(cursor.getColumnIndexOrThrow("replyCrtPostion")));
                    model.setModelId(cursor.getString(cursor.getColumnIndexOrThrow("modelId")));
                    model.setReceiverUid(cursor.getString(cursor.getColumnIndexOrThrow("receiverUid")));
                    model.setForwaredKey(cursor.getString(cursor.getColumnIndexOrThrow("forwaredKey")));
                    model.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow("groupName")));
                    model.setDocSize(cursor.getString(cursor.getColumnIndexOrThrow("docSize")));
                    model.setFileName(cursor.getString(cursor.getColumnIndexOrThrow("fileName")));
                    model.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail")));
                    model.setFileNameThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("fileNameThumbnail")));
                    model.setCaption(cursor.getString(cursor.getColumnIndexOrThrow("caption")));
                    model.setNotification(cursor.getInt(cursor.getColumnIndexOrThrow("notification")));
                    model.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow("currentDate")));
                    model.setEmojiCount(cursor.getString(cursor.getColumnIndexOrThrow("emojiCount")));
                    model.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
                    model.setImageWidth(cursor.getString(cursor.getColumnIndexOrThrow("imageWidthDp")));
                    model.setImageHeight(cursor.getString(cursor.getColumnIndexOrThrow("imageHeightDp")));
                    model.setAspectRatio(cursor.getString(cursor.getColumnIndexOrThrow("aspectRatio")));
                    model.setSelectionCount(cursor.getString(cursor.getColumnIndexOrThrow("selectionCount")));

                    // Deserialize emojiModel from JSON
                    String emojiJson = cursor.getString(cursor.getColumnIndexOrThrow("emojiModel"));
                    model.setEmojiModel(deserializeEmojiModel(emojiJson));

                    // Deserialize selectionBunch from JSON
                    String selectionBunchJson = cursor.getString(cursor.getColumnIndexOrThrow("selectionBunch"));
                    model.setSelectionBunch(deserializeSelectionBunch(selectionBunchJson));

                    list.add(model);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        Log.d("PendingMessages", "Retrieved " + list.size() + " pending messages for UID: " + receiverUid);
        return list;
    }

    /**
     * Remove a message from pending messages table (after successful upload)
     */
    public boolean removePendingMessage(String modelId, String receiverUid) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;

        try {
            String query = "DELETE FROM " + TABLE_NAME_PENDING + " WHERE modelId = ? AND receiverUid = ?";
            db.execSQL(query, new String[]{modelId, receiverUid});
            isDeleted = true;
            Log.d("PendingMessages", "Successfully removed pending message: " + modelId);
        } catch (Exception e) {
            Log.e("PendingMessages", "Error removing pending message: " + e.getMessage(), e);
        } finally {
            db.close();
        }

        return isDeleted;
    }

    /**
     * Update upload status of a pending message
     */
    public void updatePendingMessageStatus(String modelId, String receiverUid, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uploadStatus", status);

        try {
            int rowsAffected = db.update(TABLE_NAME_PENDING, values, 
                    "modelId = ? AND receiverUid = ?", 
                    new String[]{modelId, receiverUid});
            Log.d("PendingMessages", "Updated status for message " + modelId + " to " + status + 
                    " (rows affected: " + rowsAffected + ")");
        } catch (Exception e) {
            Log.e("PendingMessages", "Error updating message status: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }

    /**
     * Insert a group message into pending messages table
     */
    public void insertPendingGroupMessage(group_messageModel groupModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        // Convert group_messageModel to messageModel format for storage
        values.put("uid", groupModel.getUid());
        values.put("message", groupModel.getMessage());
        values.put("time", groupModel.getTime());
        values.put("document", groupModel.getDocument());
        values.put("dataType", groupModel.getDataType());
        values.put("extension", groupModel.getExtension());
        values.put("name", groupModel.getName());
        values.put("phone", groupModel.getPhone());
        values.put("micPhoto", groupModel.getMicPhoto());
        values.put("miceTiming", groupModel.getMiceTiming());
        values.put("userName", groupModel.getUserName());
        values.put("replytextData", "");
        values.put("replyKey", "");
        values.put("replyType", "");
        values.put("replyOldData", "");
        values.put("replyCrtPostion", "");
        values.put("modelId", groupModel.getModelId());
        values.put("grpIdKey", groupModel.getReceiverUid()); // Use grpIdKey instead of receiverUid
        values.put("forwaredKey", "");
        values.put("groupName", "");
        values.put("docSize", groupModel.getDocSize());
        values.put("fileName", groupModel.getFileName());
        values.put("thumbnail", groupModel.getThumbnail());
        values.put("fileNameThumbnail", groupModel.getFileNameThumbnail());
        values.put("caption", groupModel.getCaption());
        values.put("notification", 1);
        values.put("currentDate", groupModel.getCurrentDate());
        
        // Handle emojiModel - convert to JSON
        Gson gson = new Gson();
        String emojiJson = gson.toJson(new ArrayList<emojiModel>());
        values.put("emojiModel", emojiJson);
        values.put("emojiCount", "");
        values.put("imageWidthDp", groupModel.getImageWidth());
        values.put("imageHeightDp", groupModel.getImageHeight());
        values.put("aspectRatio", groupModel.getAspectRatio());
        values.put("timestamp", System.currentTimeMillis());
        values.put("selectionCount", groupModel.getSelectionCount());
        
        // Handle selectionBunch - convert to JSON
        String selectionBunchJson = gson.toJson(new ArrayList<>());
        values.put("selectionBunch", selectionBunchJson);
        values.put("uploadStatus", 0); // 0 = pending, 1 = uploading, 2 = completed
        
        try {
            db.insertWithOnConflict(TABLE_NAME_GROUP_PENDING, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("PendingMessages", "Group message stored in group pending table: " + groupModel.getModelId());
        } catch (Exception e) {
            Log.e("PendingMessages", "Error storing group message in group pending table: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }

    /**
     * Get pending group messages for a specific group
     */
    public ArrayList<group_messageModel> getPendingGroupMessages(String grpIdKey) {
        ArrayList<group_messageModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_GROUP_PENDING + " WHERE grpIdKey = ? AND uploadStatus IN (0,1) ORDER BY timestamp ASC";
        Cursor cursor = db.rawQuery(query, new String[]{grpIdKey});

        try {
            while (cursor.moveToNext()) {
                group_messageModel model = new group_messageModel();
                model.setUid(cursor.getString(cursor.getColumnIndexOrThrow("uid")));
                model.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
                model.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                model.setDocument(cursor.getString(cursor.getColumnIndexOrThrow("document")));
                model.setDataType(cursor.getString(cursor.getColumnIndexOrThrow("dataType")));
                model.setExtension(cursor.getString(cursor.getColumnIndexOrThrow("extension")));
                model.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                model.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                model.setMicPhoto(cursor.getString(cursor.getColumnIndexOrThrow("micPhoto")));
                model.setMiceTiming(cursor.getString(cursor.getColumnIndexOrThrow("miceTiming")));
                model.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
                model.setModelId(cursor.getString(cursor.getColumnIndexOrThrow("modelId")));
                model.setReceiverUid(cursor.getString(cursor.getColumnIndexOrThrow("grpIdKey"))); // grpIdKey is stored as receiverUid in group_messageModel
                model.setDocSize(cursor.getString(cursor.getColumnIndexOrThrow("docSize")));
                model.setFileName(cursor.getString(cursor.getColumnIndexOrThrow("fileName")));
                model.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail")));
                model.setFileNameThumbnail(cursor.getString(cursor.getColumnIndexOrThrow("fileNameThumbnail")));
                model.setCaption(cursor.getString(cursor.getColumnIndexOrThrow("caption")));
                model.setCurrentDate(cursor.getString(cursor.getColumnIndexOrThrow("currentDate")));
                model.setImageWidth(cursor.getString(cursor.getColumnIndexOrThrow("imageWidthDp")));
                model.setImageHeight(cursor.getString(cursor.getColumnIndexOrThrow("imageHeightDp")));
                model.setAspectRatio(cursor.getString(cursor.getColumnIndexOrThrow("aspectRatio")));
                model.setSelectionCount(cursor.getString(cursor.getColumnIndexOrThrow("selectionCount")));
                
                // Deserialize emojiModel from JSON
                String emojiJson = cursor.getString(cursor.getColumnIndexOrThrow("emojiModel"));
                Gson gson = new Gson();
                Type emojiListType = new TypeToken<ArrayList<emojiModel>>(){}.getType();
                ArrayList<emojiModel> emojiModels = gson.fromJson(emojiJson, emojiListType);
                if (emojiModels == null) emojiModels = new ArrayList<>();
                // Note: group_messageModel doesn't have setEmojiModel method, so we skip this
                
                // Deserialize selectionBunch from JSON
                String selectionBunchJson = cursor.getString(cursor.getColumnIndexOrThrow("selectionBunch"));
                Type selectionBunchListType = new TypeToken<ArrayList<selectionBunchModel>>(){}.getType();
                ArrayList<selectionBunchModel> selectionBunch = gson.fromJson(selectionBunchJson, selectionBunchListType);
                if (selectionBunch == null) selectionBunch = new ArrayList<>();
                model.setSelectionBunch(selectionBunch);
                
                list.add(model);
            }
        } catch (Exception e) {
            Log.e("PendingMessages", "Error retrieving group pending messages: " + e.getMessage(), e);
        } finally {
            cursor.close();
            db.close();
        }

        Log.d("PendingMessages", "Retrieved " + list.size() + " pending group messages for grpIdKey: " + grpIdKey);
        return list;
    }

    /**
     * Remove a group message from pending group messages table (after successful upload)
     */
    public boolean removePendingGroupMessage(String modelId, String grpIdKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDeleted = false;

        try {
            String query = "DELETE FROM " + TABLE_NAME_GROUP_PENDING + " WHERE modelId = ? AND grpIdKey = ?";
            db.execSQL(query, new String[]{modelId, grpIdKey});
            isDeleted = true;
            Log.d("PendingMessages", "Successfully removed pending group message: " + modelId);
        } catch (Exception e) {
            Log.e("PendingMessages", "Error removing pending group message: " + e.getMessage(), e);
        } finally {
            db.close();
        }

        return isDeleted;
    }

    /**
     * Update upload status of a pending group message
     */
    public void updatePendingGroupMessageStatus(String modelId, String grpIdKey, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uploadStatus", status);

        try {
            int rowsAffected = db.update(TABLE_NAME_GROUP_PENDING, values, 
                    "modelId = ? AND grpIdKey = ?", 
                    new String[]{modelId, grpIdKey});
            Log.d("PendingMessages", "Updated status for group message " + modelId + " to " + status + 
                    " (rows affected: " + rowsAffected + ")");
        } catch (Exception e) {
            Log.e("PendingMessages", "Error updating group message status: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }

    /**
     * Deserialize selectionBunch from JSON
     */
    private ArrayList<selectionBunchModel> deserializeSelectionBunch(String selectionBunchJson) {
        try {
            if (selectionBunchJson == null || selectionBunchJson.isEmpty()) {
                return new ArrayList<>();
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<selectionBunchModel>>(){}.getType();
            return gson.fromJson(selectionBunchJson, listType);
        } catch (Exception e) {
            Log.e("deserializeSelectionBunch", "Error deserializing selectionBunch: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

}
