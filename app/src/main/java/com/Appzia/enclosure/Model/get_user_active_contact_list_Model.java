package com.Appzia.enclosure.Model;

public class get_user_active_contact_list_Model {
    String photo, full_name, mobile_no, caption, uid,sent_time,dataType,messages,f_token;
    String msg_limit;
    String device_type;
    int notification;
    String room;
    String original_name;
    boolean block;
    boolean iamblocked;

    public get_user_active_contact_list_Model(String photo, String full_name, String mobile_no, String caption, String uid, String sent_time, String dataType, String messages, String f_token,int notification,String msg_limit,String device_type,String room,String original_name,boolean block,boolean iamblocked) {
        this.photo = photo;
        this.full_name = full_name;
        this.mobile_no = mobile_no;
        this.caption = caption;
        this.uid = uid;
        this.sent_time = sent_time;
        this.dataType = dataType;
        this.messages = messages;
        this.f_token = f_token;
        this.notification = notification;
        this.msg_limit = msg_limit;
        this.device_type = device_type;
        this.room = room;
        this.original_name = original_name;
        this.block = block;
        this.iamblocked = iamblocked;
    }

    public boolean isIamblocked() {
        return iamblocked;
    }

    public void setIamblocked(boolean iamblocked) {
        this.iamblocked = iamblocked;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }


    public String getMsg_limit() {
        return msg_limit;
    }

    public void setMsg_limit(String msg_limit) {
        this.msg_limit = msg_limit;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
