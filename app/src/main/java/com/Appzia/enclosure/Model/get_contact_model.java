package com.Appzia.enclosure.Model;

public class get_contact_model {

    String uid, photo, full_name, mobile_no, caption, f_token, device_type;
    boolean block;

    public get_contact_model() {
    }


    public get_contact_model(String uid, String photo, String full_name, String mobile_no, String caption, String f_token, String device_type,boolean block) {
        this.uid = uid;
        this.photo = photo;
        this.full_name = full_name;
        this.mobile_no = mobile_no;
        this.caption = caption;
        this.f_token = f_token;
        this.device_type = device_type;
        this.block = block;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
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
}
