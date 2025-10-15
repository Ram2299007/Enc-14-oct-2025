package com.Appzia.enclosure.Model;

public class allContactListModel {

    String c_flag;
    String uid;
    String photo;
    String full_name;
    String mobile_no;
    String caption;
    String f_token;
    String device_type;
    String contact_name;
    String contact_number;
    boolean block;
    boolean isIamblocked;

    public allContactListModel(String c_flag, String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type, String contact_name, String contact_number,boolean block,boolean isIamblocked) {
        this.c_flag = c_flag;
        this.uid = uid;
        this.photo = photo;
        this.full_name = full_name;
        this.mobile_no = mobile_no;
        this.caption = caption;
        this.f_token = f_token;
        this.device_type = device_type;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.block = block;
        this.isIamblocked = isIamblocked;
    }


    public boolean isIamblocked() {
        return isIamblocked;
    }

    public void setIamblocked(boolean iamblocked) {
        isIamblocked = iamblocked;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public String getC_flag() {
        return c_flag;
    }

    public void setC_flag(String c_flag) {
        this.c_flag = c_flag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
