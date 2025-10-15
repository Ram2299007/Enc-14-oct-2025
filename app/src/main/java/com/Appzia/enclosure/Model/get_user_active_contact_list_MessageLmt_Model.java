package com.Appzia.enclosure.Model;

public class get_user_active_contact_list_MessageLmt_Model {
    String msgLmt;
    String photo, full_name, mobile_no, caption, uid;
    boolean block;

    public get_user_active_contact_list_MessageLmt_Model(String photo, String full_name, String mobile_no, String caption, String uid, String msgLmt,boolean block) {
        this.photo = photo;
        this.full_name = full_name;
        this.mobile_no = mobile_no;
        this.caption = caption;
        this.uid = uid;
        this.msgLmt = msgLmt;
        this.block = block;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public get_user_active_contact_list_MessageLmt_Model(String full_name) {
        this.full_name = full_name;
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

    public String getMsgLmt() {
        return msgLmt;
    }

    public void setMsgLmt(String msgLmt) {
        this.msgLmt = msgLmt;
    }
}
